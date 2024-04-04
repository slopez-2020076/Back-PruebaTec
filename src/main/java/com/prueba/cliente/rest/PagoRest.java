package com.prueba.cliente.rest;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.prueba.cliente.model.Pago;
import com.prueba.cliente.service.PagoService;

@RestController
@RequestMapping("/Pagos/")
public class PagoRest {

	@Autowired
	private PagoService pagosService;
	
	
	//LISATAR TODOS LOS PAGOS
	@GetMapping
	private ResponseEntity<List<Pago>> getAllPagos(){
		return ResponseEntity.ok(pagosService.findAll());
	}
	
	
	//GUARDAR PAGO
	@PostMapping ("GuardarPago")
	private Pago guardarPago(@RequestBody Pago pago) {
	    try {
	        Pago pagoGuardado = pagosService.guardarPago(pago);
	        return pagoGuardado;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("No se pudo guardar el Pago", e);
	    }
	}
	
	
	
	//ELIMIANR PAGO
	@DeleteMapping(value = "deletePago/{id}")
	public ResponseEntity<Void> deletePago(@PathVariable("id") Integer id) {
		pagosService.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
	
	
	
}
