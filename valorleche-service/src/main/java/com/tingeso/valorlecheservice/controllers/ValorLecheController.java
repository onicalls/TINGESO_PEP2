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

    @GetMapping("/{quincena}")
    public ResponseEntity<List<ValorLecheEntity>> listaValorLecheQuincena(@PathVariable("quincena") String quincena) {
        List<ValorLecheEntity> lista = valorLecheService.buscarPorQuincena(quincena);
        if(lista == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{proveedor}/{quincenaActual}")
    public ResponseEntity<ValorLecheEntity> findByProveedorAndQuincena(
            @PathVariable("proveedor") String proveedor, @PathVariable("quincenaActual") String quincenaActual)
    {
        ValorLecheEntity valorLecheEntidad = valorLecheService.findByProveedorAndQuincena(proveedor, quincenaActual);
        return ResponseEntity.ok(valorLecheEntidad);
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
