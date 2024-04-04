package com.prueba.cliente.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.cliente.model.Pago;


@Repository
public interface PagoRepository extends JpaRepository <Pago, Integer> {
	
	
}
