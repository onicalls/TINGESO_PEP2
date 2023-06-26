package com.tingeso.valorlecheservice.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "valorleche")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
public class ValorLecheEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String proveedor;
    private double grasa;
    private double solido;
    private String quincena;
}