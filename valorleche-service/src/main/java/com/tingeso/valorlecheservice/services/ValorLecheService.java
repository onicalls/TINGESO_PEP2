package com.tingeso.valorlecheservice.services;


import com.tingeso.valorlecheservice.entities.ValorLecheEntity;
import com.tingeso.valorlecheservice.models.AcopioModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.tingeso.valorlecheservice.repositories.ValorLecheRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValorLecheService {

    @Autowired
    private ValorLecheRepository valorLecheRepository;

    @Autowired
    RestTemplate restTemplate;

    private final Logger logg = LoggerFactory.getLogger(ValorLecheService.class);

    public ArrayList<ValorLecheEntity> obtenerData() {
        return (ArrayList<ValorLecheEntity>) valorLecheRepository.findAll();
    }

    public void guardar(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            if ((!file.isEmpty()) && (filename.toUpperCase().equals("VALORLECHE.CSV"))) {
                try {
                    byte[] bytes = file .getBytes();
                    Path path = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                } catch (IOException e) {
                    logg.error("ERROR", e);
                }
            }
        } else {
        }
    }


    public ValorLecheEntity obtenerEspecifico2(String rut, String fecha){
        return valorLecheRepository.buscarData2(rut, fecha);
    }

    public void leerCsv(String direccion, int year, int month, String quincena) {
        String texto = "";
        BufferedReader bf = null;
        valorLecheRepository.deleteAll();
        try {
            bf = new BufferedReader(new FileReader(direccion));
            bf.readLine();
            String temp = "";
            String bfRead;
            while ((bfRead = bf.readLine()) != null) {
                guardarDataDB(bfRead.split(",")[0], Double.parseDouble(bfRead.split(",")[1]), Double.parseDouble(bfRead.split(",")[2]), formatQuincena(year, month, quincena));
                temp = temp + "\n" + bfRead;
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        } catch (Exception e) {
            System.err.println("No se encontro el archivo");
        } finally {
            calcularKilosTotalesQuincena(year, month, quincena);
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    logg.error("ERROR", e);
                }
            }
        }
    }

    public void guardarData(ValorLecheEntity data) {
        valorLecheRepository.save(data);
    }

    public void guardarDataDB(String proveedor, double grasa, double solido, String quincena) {
        ValorLecheEntity newData = new ValorLecheEntity();
        newData.setProveedor(proveedor);
        newData.setGrasa(grasa);
        newData.setSolido(solido);
        newData.setQuincena(quincena);
        guardarData(newData);
    }


    public String obtenerFechaRut(String rut){
        return valorLecheRepository.buscarFechaRut(rut);
    }

    public List<String> obtenerRuts() {
        return valorLecheRepository.findDistinctRut();
    }

    public void eliminarData(ArrayList<ValorLecheEntity> datas){
        valorLecheRepository.deleteAll(datas);
    }

    private String formatQuincena(int year, int month, String quincena) {
        String monthString = String.format("%02d", month);
        return year + "-" + monthString + "-" + quincena;
    }

    public List<AcopioModel> acopioEnRango(LocalDate startDate, LocalDate endDate) {
        String url = "http://acopio-service/acopio/listaacopios/" + startDate.toString() + "/" + endDate.toString();
        ResponseEntity<List<AcopioModel>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AcopioModel>>() {}
        );
        List<AcopioModel> acopios = response.getBody();
        return acopios;
    }


    public void calcularKilosTotalesQuincena(int year, int month, String quincena) {
        LocalDate startDate;
        LocalDate endDate;

        if (quincena.equals("Q1")) {
            startDate = LocalDate.of(year, month, 1);
            endDate = LocalDate.of(year, month, 15);
        } else if (quincena.equals("Q2")) {
            YearMonth yearMonthObject = YearMonth.of(year, month);
            int daysInMonth = yearMonthObject.lengthOfMonth();
            startDate = LocalDate.of(year, month, 16);
            endDate = LocalDate.of(year, month, daysInMonth);
        } else {
            throw new IllegalArgumentException("Invalid quincena: " + quincena);
        }

        List<AcopioModel> acopioEntities = acopioEnRango(startDate, endDate);

        Map<String, ProviderData> providerDataMap = new HashMap<>();
        System.out.println("Checkpoint 1\n");
        for (AcopioModel acopio : acopioEntities) {

            System.out.println("Checkpoint 2\n");
            System.out.println("Acopio " + acopio.toString());
            String provider = acopio.getProveedor();
            ProviderData providerData = providerDataMap.getOrDefault(provider, new ProviderData());
            providerData.addKilos(acopio.getKlsLeche());
            providerData.addDelivery(acopio.getTurno());
            providerDataMap.put(provider, providerData);
        }
        System.out.println("Checkpoint 3\n");
        for (Map.Entry<String, ProviderData> entry : providerDataMap.entrySet()) {
            String provider = entry.getKey();
            ProviderData providerData = entry.getValue();

            ValorLecheEntity valorLeche = valorLecheRepository.findByProveedorAndQuincena(provider, formatQuincena(year, month, quincena));
            if (valorLeche == null) {
                valorLeche = new ValorLecheEntity();
                valorLeche.setProveedor(provider);
                valorLeche.setQuincena(formatQuincena(year, month, quincena));
            }

            valorLeche.setKilos(providerData.getTotalKilos());
            valorLeche.setDiasTotalesAcopio(providerData.totalDeliveries);
            valorLeche.setPromedioKilosAcopio(providerData.totalKilos / providerData.totalDeliveries);
            valorLeche.setConstancia(providerData.getFrecuencia());

            valorLecheRepository.save(valorLeche);
        }
    }

    private static class ProviderData {
        private double totalKilos = 0;
        private int totalDeliveries = 0;
        private int morningDeliveries = 0;
        private int afternoonDeliveries = 0;

        public void addDelivery(String turno) {
            totalDeliveries++;
            if (turno.equals("M")) {
                morningDeliveries++;
            } else if (turno.equals("T")) {
                afternoonDeliveries++;
            }
        }

        public String getFrecuencia() {
            if (totalDeliveries >= 10) {
                if (morningDeliveries > 0 && afternoonDeliveries > 0) {
                    return "MT";
                } else if (morningDeliveries > 0) {
                    return "M";
                } else if (afternoonDeliveries > 0) {
                    return "T";
                }
            }
            return "NO";
        }

        void addKilos(double kilos) {
            this.totalKilos += kilos;
        }

        double getTotalKilos() {
            return totalKilos;
        }
    }
}
