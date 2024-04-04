package com.prueba.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.cliente.model.Estado;


@Repository
public interface EstadoRepository  extends JpaRepository <Estado, Integer>{

}
