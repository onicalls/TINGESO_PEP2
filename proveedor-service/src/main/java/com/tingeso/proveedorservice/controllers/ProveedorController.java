package com.tingeso.proveedorservice.controllers;

import com.tingeso.proveedorservice.entities.ProveedorEntity;
import com.tingeso.proveedorservice.models.AutorizacionModel;
import com.tingeso.proveedorservice.models.JustificativoModel;
import com.tingeso.proveedorservice.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {

    @Autowired
    ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorEntity>> obtenerProveedores(){
        List<ProveedorEntity> proveedors = proveedorService.obtenerProveedores();
        if(proveedors.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(proveedors);
    }
    @GetMapping("/{proveedor}")
    public ResponseEntity<ProveedorEntity> obtenerPorCodigo(@PathVariable("proveedor") String proveedor){
        ProveedorEntity proveedorListo = proveedorService.findByCodigo(proveedor);
        if(proveedorListo == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(proveedorListo);
    }

    @PostMapping
    public void guardarproveedor(@RequestBody ProveedorEntity proveedor){
        proveedorService.guardarproveedor(proveedor);
    }

    @GetMapping("/eliminar")
    public void eliminarproveedors(){
        proveedorService.eliminarproveedors();
    }

    @GetMapping("/justificativos/{rut}")
    public ResponseEntity<List<JustificativoModel>> obtenerJustificativos(@PathVariable("rut") String rut) {
        ProveedorEntity proveedor = proveedorService.findByCodigo(rut);
        if(proveedor == null)
            return ResponseEntity.notFound().build();
        List<JustificativoModel> justificativos = proveedorService.obtenerJustificativos(rut);
        return ResponseEntity.ok(justificativos);
    }

    @GetMapping("/autorizaciones/{rut}")
    public ResponseEntity<List<AutorizacionModel>> obtenerAutorizaciones(@PathVariable("rut") String rut) {
        ProveedorEntity proveedor = proveedorService.findByCodigo(rut);
        if(proveedor == null)
            return ResponseEntity.notFound().build();
        List<AutorizacionModel> autorizaciones = proveedorService.obtenerAutorizaciones(rut);
        return ResponseEntity.ok(autorizaciones);
    }

    @PostMapping("/addProveedor")
    public String addProveedor(@RequestParam("codigo") String codigo,
                               @RequestParam("nombre") String nombre,
                               @RequestParam("categoria") String categoria,
                               @RequestParam("retencion") String retencion) {
        proveedorService.guardarProveedor(codigo, nombre, categoria, retencion);
        return "redirect:/proveedor";
    }
}
