package com.tingeso.pagoservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValorLecheModel {
    private String proveedor;
    private double grasa;
    private double solido;
    private double kilos;
    private String quincena;
}