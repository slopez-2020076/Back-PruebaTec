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
@Table (name = "Prestamo")
public class Prestamo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id; 
	private Double monto; 
	private Double saldo;
	private Integer plazo;//Cantidad de Pagos 
	private Integer pagos;
	private String fecha_final;
	private String fecha_inicial;
	private String descripcion;
	
	
	@ManyToOne
	@JoinColumn (name = "id_cliente")
	private Cliente cliente;
	
	
	@ManyToOne
	@JoinColumn (name = "estado_id")
	private Estado estado_id;
	
	
}
