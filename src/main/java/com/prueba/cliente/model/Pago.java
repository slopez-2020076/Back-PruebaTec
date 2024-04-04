package com.prueba.cliente.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table (name = "Pagos")
public class Pago implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id; 
	private Double montoPago;
	private String fechaPago;
	
	@ManyToOne
	@JoinColumn (name = "id_metodPago")
	private MetodoPago nombre_metodo;
	
	
	@ManyToOne
	@JoinColumn (name = "id_prestamo")
	private Prestamo numero_prestamo;
	
}
