package com.prueba.cliente.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.cliente.model.Estado;
import com.prueba.cliente.service.EstadoService;
import com.prueba.cliente.service.EstadoService.EstadoNoEncontradoException;

@RestController
@RequestMapping("/Estado/")
public class EstadoRest {
	
	@Autowired
	private EstadoService estadoService;
	
	
	//MOSTRAR TODOS LOS ESTADO DE PRESTAMO
	@GetMapping
	private ResponseEntity<List<Estado>> getAllStatus(){
		return ResponseEntity.ok(estadoService.findAll());
	}
	
	
	//ELIMINAR ESTADOS DE PRESTAMO
	@DeleteMapping(value = "delete/{id}")
	public ResponseEntity<Void> deleteEstado(@PathVariable("id") Integer id) {
		estadoService.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
	
	
	//Exception EstadoNotFound
	@ExceptionHandler(EstadoNoEncontradoException.class)
	public ResponseEntity<String> handleEstadoNoEncontradoException(EstadoNoEncontradoException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
			

}
