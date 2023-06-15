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
    private ValorLecheRepository marcasRelojRepository;

    private final Logger logg = LoggerFactory.getLogger(ValorLecheService.class);

    public ArrayList<ValorLecheEntity> obtenerData() {
        return (ArrayList<ValorLecheEntity>) marcasRelojRepository.findAll();
    }

    public String guardar(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            if ((!file.isEmpty()) && (filename.toUpperCase().equals("DATA.TXT"))) {
                try {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                } catch (IOException e) {
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        } else {
            return "No se pudo guardar el archivo";
        }
    }

    public ValorLecheEntity obtenerEspecifico(String rut, String fecha){
        return marcasRelojRepository.buscarData(rut, fecha);
    }

    public ValorLecheEntity obtenerEspecifico2(String rut, String fecha){
        return marcasRelojRepository.buscarData2(rut, fecha);
    }

    public void leerTxt(String direccion) {
        String texto = "";
        BufferedReader bf = null;
        marcasRelojRepository.deleteAll();
        try {
            bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            while ((bfRead = bf.readLine()) != null) {
                String fecha = bfRead.split(";")[0];
                String newFecha = fecha.replaceAll("/","-");
                guardarDataDB(newFecha, bfRead.split(";")[1], bfRead.split(";")[2]);
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
        marcasRelojRepository.save(data);
    }

    public void guardarDataDB(String fecha, String hora, String rut) {
        ValorLecheEntity newData = new ValorLecheEntity();
        newData.setFecha(fecha);
        newData.setRut(rut);
        newData.setHora(hora);
        guardarData(newData);
    }

    public String obtenerFechaRut(String rut){
        return marcasRelojRepository.buscarFechaRut(rut);
    }

    public List<String> obtenerRuts() {
        return marcasRelojRepository.findDistinctRut();
    }

    public void eliminarData(ArrayList<ValorLecheEntity> datas){
        marcasRelojRepository.deleteAll(datas);
    }
}
