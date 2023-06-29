package com.tingeso.pagoservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValorLecheModel {
    private Long id;
    private String proveedor;
    private double grasa;
    private double solido;
    private String quincena;
    private double kilos;
    private String constancia;
    private double diasTotalesAcopio;
    private double promedioKilosAcopio;
}