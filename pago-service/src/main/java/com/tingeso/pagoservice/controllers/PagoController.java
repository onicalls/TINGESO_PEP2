package com.tingeso.pagoservice.controllers;

import com.tingeso.pagoservice.entities.PagoEntity;
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

    @PostMapping("/generarPagos")
    public String generarPlanillaPagos(@RequestParam("anio") int anio,
                                       @RequestParam("mes") int mes,
                                       @RequestParam("quin") String quin,
                                       Model model) {
        pagoService.generarPagos(anio, mes, quin);
        return "redirect:/pagos";
    }
}
