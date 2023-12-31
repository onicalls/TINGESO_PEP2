package com.tingeso.acopioservice.services;


import com.tingeso.acopioservice.entities.AcopioEntity;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tingeso.acopioservice.repositories.AcopioRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AcopioService {

    @Autowired
    private AcopioRepository acopioRepository;

    private final Logger logg = LoggerFactory.getLogger(AcopioService.class);

    public ArrayList<AcopioEntity> obtenerData() {
        return (ArrayList<AcopioEntity>) acopioRepository.findAll();
    }

    public void guardar(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            if ((!file.isEmpty()) && (filename.toUpperCase().equals("ACOPIO.CSV"))) {
                try {
                    byte[] bytes = file.getBytes();
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

    public void leerCsv(String direccion) {
        String texto = "";
        BufferedReader bf = null;
        acopioRepository.deleteAll();
        try {
            bf = new BufferedReader(new FileReader(direccion));
            bf.readLine();
            String temp = "";
            String bfRead;
            while ((bfRead = bf.readLine()) != null) {
                System.out.println(bfRead);
                String fecha = bfRead.split(",")[0];
                String newFecha = fecha.replaceAll("/","-");
                guardarDataDB(newFecha, bfRead.split(",")[1], bfRead.split(",")[2], Double.parseDouble(bfRead.split(",")[3]));
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

    public void guardarData(AcopioEntity data) {
        acopioRepository.save(data);
    }

    public void guardarDataDB(String fecha, String turno, String proveedor, double klsLeche) {
        AcopioEntity newData = new AcopioEntity();
        newData.setFecha(fecha);
        newData.setTurno(turno);
        newData.setProveedor(proveedor);
        newData.setKlsLeche(klsLeche);
        guardarData(newData);
    }

    public List<String> obtenerProveedores() {
        return acopioRepository.findDistinctProveedor();
    }

    public List<AcopioEntity> obtenerProveedoresPorFecha(String start, String end) {
        return acopioRepository.findAcopioByDateBetween(start, end);
    }

    public void eliminarData(ArrayList<AcopioEntity> datas){
        acopioRepository.deleteAll(datas);
    }
}
