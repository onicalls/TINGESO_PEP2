package com.tingeso.valorlecheservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcopioModel {
    private String proveedor;
    private String turno;
    private double klsLeche;
    private LocalDate fecha;

    @Override
    public String toString() {
        return "AcopioModel{" +
                "proveedor='" + proveedor + '\'' +
                ", turno='" + turno + '\'' +
                ", klsLeche=" + klsLeche +
                ", fecha=" + fecha +
                '}';
    }
}
