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

import com.prueba.cliente.model.MetodoPago;
import com.prueba.cliente.service.MetodoPagoService;
import com.prueba.cliente.service.MetodoPagoService.MetodoPagoNoEncontradoException;

@RestController
@RequestMapping("/MetodoPago/")
public class MetodoPagoRest {
	
	
	@Autowired
	private MetodoPagoService metodoPagoService;
	
	
	//LISTAR TODOS LOS METODOS DE PAGO
	@GetMapping
	private ResponseEntity<List<MetodoPago>> getAllMetodoPagos(){
		return ResponseEntity.ok(metodoPagoService.findAll());
	}
	
	
	//ELIMINAR METODO DE PAGO
	@DeleteMapping(value = "delete/{id}")
	public ResponseEntity<Void> deleteMetodoPago(@PathVariable("id") Integer id) {
	    metodoPagoService.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
	
	
	//Exception MetodoPagoNotFound
	@ExceptionHandler(MetodoPagoNoEncontradoException.class)
	public ResponseEntity<String> handleMetodoPagoNoEncontradoException(MetodoPagoNoEncontradoException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	
}
