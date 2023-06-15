package com.tingeso.pagoservice.repositories;

import com.tingeso.pagoservice.entities.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, String> {

    public PagoEntity findByRut(String rut);

    @Query(value = "insert into planilla_sueldos(rut) values(?)",
            nativeQuery = true)
    void insertarDatos(@Param("rut") String rut);
}