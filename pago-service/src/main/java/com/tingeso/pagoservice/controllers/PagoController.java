package com.tingeso.pagoservice.controllers;

import com.tingeso.pagoservice.entities.PagoEntity;
import com.tingeso.pagoservice.models.PagoModel;
import com.tingeso.pagoservice.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoEntity>> obtenerPago(){
        List<PagoEntity> proveedors = pagoService.obtenerPagos();
        if(proveedors.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(proveedors);
    }

    @PostMapping
    public String getSueldos(@RequestBody PagoModel pagoModel, Model model) {
        int year = pagoModel.getYear();
        int month = pagoModel.getMonth();
        String quin = pagoModel.getQuin();
        pagoService.generarPagos(year, month, quin);
        return "ok";
    }

}
