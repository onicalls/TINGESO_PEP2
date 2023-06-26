package com.tingeso.valorlecheservice.controllers;

import com.tingeso.valorlecheservice.entities.ValorLecheEntity;
import com.tingeso.valorlecheservice.services.ValorLecheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/valorleche")
public class ValorLecheController {

    @Autowired
    private ValorLecheService valorLecheService;

    @GetMapping
    public ResponseEntity<ArrayList<ValorLecheEntity>> obtenerData() {
        ArrayList<ValorLecheEntity> data = valorLecheService.obtenerData();
        if (data.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/rut")
    public ResponseEntity<List<String>> obtenerRutsDeData(){
        List<String> ruts = valorLecheService.obtenerRuts();
        if(ruts.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ruts);
    }

    @GetMapping("/entrada/{rut}/{fecha}")
    public ResponseEntity<ValorLecheEntity> obtenerHoraEntradaPorRut(
            @PathVariable("rut") String rut, @PathVariable("fecha") String fecha)
    {
        ValorLecheEntity marca_entrada = valorLecheService.obtenerEspecifico(rut, fecha);
        return ResponseEntity.ok(marca_entrada);
    }

    @GetMapping("/primeraasistencia/{rut}")
    public ResponseEntity<String> obtenerPrimeraAsistencia(@PathVariable("rut") String rut){
     String fecha = valorLecheService.obtenerFechaRut(rut);
     if (fecha.isEmpty()){
         return ResponseEntity.noContent().build();
     }
     return ResponseEntity.ok(fecha);
    }

    @GetMapping("/salida/{rut}/{fecha}")
    public ResponseEntity<ValorLecheEntity> obtenerHoraSalidaPorRut(
            @PathVariable("rut") String rut, @PathVariable("fecha") String fecha)
    {
        ValorLecheEntity marca_salida = valorLecheService.obtenerEspecifico2(rut, fecha);
        return ResponseEntity.ok(marca_salida);
    }

    @PostMapping
    public void guardarData(@RequestParam("file") MultipartFile file,
                            @RequestParam("year") int year,
                            @RequestParam("month") int month,
                            @RequestParam("quincena") String quincena,
                            RedirectAttributes ms) throws FileNotFoundException, ParseException{
        valorLecheService.guardar(file);
        System.out.println("La quincena es" + quincena);
        valorLecheService.leerCsv("valorleche.csv", year, month, quincena);
    }

}
