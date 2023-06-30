package com.tingeso.acopioservice.controllers;

import com.tingeso.acopioservice.entities.AcopioEntity;
import com.tingeso.acopioservice.services.AcopioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/acopio")
public class AcopioController {

    @Autowired
    private AcopioService acopioRepository;

    @GetMapping
    public ResponseEntity<ArrayList<AcopioEntity>> obtenerData() {
        ArrayList<AcopioEntity> data = acopioRepository.obtenerData();
        if (data.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/proveedor")
    public ResponseEntity<List<String>> obtenerProveedoresDeData(){
        List<String> ruts = acopioRepository.obtenerProveedores();
        if(ruts.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ruts);
    }

    @GetMapping("/listaacopios/{startDate}/{endDate}")
    public ResponseEntity<List<AcopioEntity>> acopioEnRango(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
        List<AcopioEntity> acopios = acopioRepository.obtenerProveedoresPorFecha(startDate, endDate);
        if(acopios == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(acopios);
    }


    @GetMapping("/primeraasistencia/{rut}")
    public ResponseEntity<String> obtenerPrimeraAsistencia(@PathVariable("rut") String rut){
     String fecha = acopioRepository.obtenerFechaRut(rut);
     if (fecha.isEmpty()){
         return ResponseEntity.noContent().build();
     }
     return ResponseEntity.ok(fecha);
    }

    @GetMapping("/salida/{rut}/{fecha}")
    public ResponseEntity<AcopioEntity> obtenerHoraSalidaPorRut(
            @PathVariable("rut") String rut, @PathVariable("fecha") String fecha)
    {
        AcopioEntity marca_salida = acopioRepository.obtenerEspecifico2(rut, fecha);
        return ResponseEntity.ok(marca_salida);
    }

    @PostMapping
    public void guardarData(@RequestParam("file") MultipartFile file, RedirectAttributes ms) throws FileNotFoundException, ParseException{
        acopioRepository.guardar(file);
        acopioRepository.leerCsv("acopio.csv");
    }
}
