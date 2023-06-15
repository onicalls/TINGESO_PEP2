package com.tingeso.valorlecheservice.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "acopio")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ValorLecheEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private String fecha;
    private String hora;
    private String rut;
}