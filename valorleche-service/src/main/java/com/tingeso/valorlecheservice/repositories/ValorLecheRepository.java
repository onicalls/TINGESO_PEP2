package com.tingeso.valorlecheservice.repositories;

import com.tingeso.valorlecheservice.entities.ValorLecheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ValorLecheRepository extends JpaRepository <ValorLecheEntity, Integer>{

    @Query(value = "SELECT * FROM valorleche WHERE quincena LIKE CONCAT('%-', ?1)", nativeQuery = true)
    List<ValorLecheEntity> findByQuincena(String quincena);

    @Query(value = "SELECT * FROM valorleche WHERE proveedor = ?1 AND quincena = ?2", nativeQuery = true)
    ValorLecheEntity findByProveedorAndQuincena(String proveedor, String quincena);

    @Query(value = "select * from valorleche as e where e.rut = :rut and e.fecha = :fecha order by e.hora desc limit 1",  nativeQuery = true)
    ValorLecheEntity buscarData2(@Param("rut")String rut, @Param("fecha") String fecha);

    @Query(value = "select *  from valorleche as e where e.rut = :rut", nativeQuery = true)
    ArrayList<ValorLecheEntity> eliminarData(@Param("rut")String rut);
}