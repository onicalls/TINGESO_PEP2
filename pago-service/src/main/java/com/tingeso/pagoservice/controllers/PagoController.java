package com.tingeso.pagoservice.controllers;

import com.tingeso.pagoservice.entities.PagoEntity;
import com.tingeso.pagoservice.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;

@RestController
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    PagoService pagoService;

    @GetMapping
    public ResponseEntity<ArrayList<PagoEntity>> planillaDeSueldos() throws ParseException {
        pagoService.reportePlanilla();
        ArrayList<PagoEntity> reporteSueldos = pagoService.obtenerData();
        if(reporteSueldos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporteSueldos);

    }
}
