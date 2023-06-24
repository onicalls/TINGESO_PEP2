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

    public String obtenerCategoria(String rut){
        return proveedorRepository.findCategory(rut);
    }

    public ProveedorEntity findByCodigo(String codigo){
        return proveedorRepository.findByCodigo(codigo);
    }

    public void guardarproveedor(ProveedorEntity proveedor){
        proveedorRepository.save(proveedor);
    }

    public void eliminarproveedors(){
        proveedorRepository.deleteAll();
    }

    public List<JustificativoModel> obtenerJustificativos(String rut) {
        List<JustificativoModel> justificativos = restTemplate.getForObject("http://justificativo-service/justificativos/porproveedores/" + rut, List.class);
        return justificativos;
    }
    public List<AutorizacionModel> obtenerAutorizaciones(String rut) {
        List<AutorizacionModel> autorizaciones = restTemplate.getForObject("http://autorizacion-service/autorizaciones/porproveedores/" + rut, List.class);
        return autorizaciones;
    }
}