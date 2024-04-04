package com.prueba.cliente.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.cliente.model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository <Cliente, Integer> {

	
	Cliente findFirstByDpi(String dpi);

	
}
