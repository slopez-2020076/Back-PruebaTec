package com.prueba.cliente.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.cliente.model.Prestamo;


@Repository
public interface PrestamoRepository extends JpaRepository <Prestamo, Integer> {

	List<Prestamo> findAllByClienteId(Integer clienteId);

}
