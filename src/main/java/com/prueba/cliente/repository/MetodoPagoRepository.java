package com.prueba.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.cliente.model.MetodoPago;


@Repository
public interface MetodoPagoRepository extends JpaRepository <MetodoPago, Integer>{

}
