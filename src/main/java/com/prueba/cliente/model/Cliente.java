package com.prueba.cliente.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Data
@Table (name = "Cliente")
public class Cliente implements Serializable {

	
	private static final long serialVersionUID = 2L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id; 
	private String nombre; 
	private String apellido;
	private String dpi;
	private String fecha_nacimiento;
	private String direccion;
	private String telefono;
	private String correo_electronico;
	private boolean estado;
	
}
