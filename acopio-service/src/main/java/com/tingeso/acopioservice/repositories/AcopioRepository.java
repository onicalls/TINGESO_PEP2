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

    @Query(value = "SELECT * FROM acopio WHERE fecha BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<AcopioEntity> findAcopioByDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "select distinct proveedor from acopio", nativeQuery = true)
    List<String> findDistinctProveedor();

    @Query(value = "select *  from acopio as e where e.proveedor = :proveedor", nativeQuery = true)
    ArrayList<AcopioEntity> eliminarData(@Param("proveedor") String proveedor);
}