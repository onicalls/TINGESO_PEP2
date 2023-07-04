package com.tingeso.proveedorservice.services;


import com.tingeso.proveedorservice.entities.ProveedorEntity;
import com.tingeso.proveedorservice.models.AutorizacionModel;
import com.tingeso.proveedorservice.models.JustificativoModel;
import com.tingeso.proveedorservice.reporitories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProveedorService {

    @Autowired
    ProveedorRepository proveedorRepository;

    @Autowired
    RestTemplate restTemplate;

    public ArrayList<ProveedorEntity> obtenerProveedores(){
        return (ArrayList<ProveedorEntity>) proveedorRepository.findAll();
    }

    public ProveedorEntity findByCodigo(String codigo){
        return proveedorRepository.findByCodigo(codigo);
    }

    public void guardarProveedor(ProveedorEntity proveedor){
        proveedorRepository.save(proveedor);
    }

    public void eliminarproveedors(){
        proveedorRepository.deleteAll();
    }


    public void guardarProveedor(String codigo, String nombre, String categoria, String retencion) {
        ProveedorEntity proveedor = new ProveedorEntity(codigo, nombre, categoria, retencion);
        proveedorRepository.save(proveedor);
    }

}