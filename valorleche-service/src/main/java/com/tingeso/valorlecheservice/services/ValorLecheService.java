package com.tingeso.valorlecheservice.services;


import com.tingeso.valorlecheservice.entities.ValorLecheEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tingeso.valorlecheservice.repositories.ValorLecheRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValorLecheService {

    @Autowired
    private ValorLecheRepository valorLecheRepository;

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

    public ValorLecheEntity obtenerEspecifico(String rut, String fecha){
        return valorLecheRepository.buscarData(rut, fecha);
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
}
