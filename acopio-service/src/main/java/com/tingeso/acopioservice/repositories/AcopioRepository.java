package com.tingeso.acopioservice.repositories;

import com.tingeso.acopioservice.entities.AcopioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface AcopioRepository extends JpaRepository <AcopioEntity, Integer>{

    @Query(value = "select * from acopio as e where e.proveedor = :proveedor and e.turno =:turno and e.klsLeche =:klsLeche and e.fecha =:fecha limit 1",
            nativeQuery = true)
    AcopioEntity buscarData(@Param("proveedor") String proveedor, @Param("turno") String turno, @Param("klsLeche") Double klsLeche, @Param("fecha") String fecha);

    @Query(value = "SELECT * FROM acopio WHERE fecha BETWEEN :start AND :end", nativeQuery = true)
    List<AcopioEntity> findAcopioByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query(value = "select distinct proveedor from acopio", nativeQuery = true)
    List<String> findDistinctProveedor();

    @Query(value = "select e.fecha from acopio as e where e.rut = :rut limit 1", nativeQuery = true)
    String buscarFechaRut(@Param("rut")String rut);

    @Query(value = "select * from acopio as e where e.rut = :rut and e.fecha = :fecha order by e.hora desc limit 1",  nativeQuery = true)
    AcopioEntity buscarData2(@Param("rut")String rut, @Param("fecha") String fecha);

    @Query(value = "select *  from acopio as e where e.proveedor = :proveedor", nativeQuery = true)
    ArrayList<AcopioEntity> eliminarData(@Param("proveedor") String proveedor);
}