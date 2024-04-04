package com.prueba.cliente.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.prueba.cliente.model.Prestamo;
import com.prueba.cliente.service.PrestamoService;
import com.prueba.cliente.service.PrestamoService.PrestamoNoEncontradoException;;

@RestController
@RequestMapping("/Prestamo/")
public class PrestamoRest {

	@Autowired
	private PrestamoService prestamoService;
	
	//MOSTAR TODOS LOS PRESTAMOS
	@GetMapping
	private ResponseEntity<List<Prestamo>> getAllPrestamos(){
		return ResponseEntity.ok(prestamoService.findAll());
	}
		
		
	//GUARDAR PRESTAMO
	@PostMapping("GuardarPrestamo")
	public ResponseEntity< Prestamo> savePrestamo(@RequestBody Prestamo prestamo) {
	    try {
	        Prestamo prestamoGuardado = prestamoService.savePrestamo(prestamo);
	        return ResponseEntity.ok(prestamoGuardado);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("No se pudo guardar el cliente", e);
	    }
	}
		
		
	//ACTUALLIZAR ESTADO Y DESCRIPCION 
	@PutMapping("ActualizarEstado/{id}")
	public ResponseEntity<String> editarPrestamo(@RequestBody Prestamo prestamoActualizado, @PathVariable ("id") Integer id) {
		try {
			prestamoService.actualizarEstado(prestamoActualizado, id);
	        return ResponseEntity.ok("Prestamo actualizado correctamente");
	    } catch (PrestamoNoEncontradoException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}
		
		
	//CONSULTA SALDO PARA UN PRESTAMO 
	@GetMapping("CalculoSaldo/{id}")
    public ResponseEntity<Map<String, Object>> calculoSaldo(@PathVariable("id") Integer id) {
        try {
            Map<String, Object> detallesPrestamo = prestamoService.calculateSaldo(id);
            return ResponseEntity.ok(detallesPrestamo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo calcular el saldo del préstamo", e);
        }
    }
	
	
	//CONSULTA SALDO PARA TODOS LOS PRETAMOS 
	@GetMapping("CalculoSaldosAprobados")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> calculoAprobados() {
        try {
            Map<String, List<Map<String, Object>>> detallesPrestamos = prestamoService.calculateSaldosAprobados();
            return ResponseEntity.ok(detallesPrestamos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo calcular el saldo de los préstamos aprobados", e);
        }
    }
		
	
	//PRETAMOS APROBADOS POR CLIENTE 
	@GetMapping("PrestmosClienteAprobados/{id}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> ListadoPrestamosAprobadosCliente(@PathVariable ("id") Integer  idCliente) {
        try {
            Map<String, List<Map<String, Object>>> detallesPrestamos = prestamoService.ListadoPrestamosAprobadosCliente(idCliente);
            return ResponseEntity.ok(detallesPrestamos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo los préstamos aprobados", e);
        }
    }
	
	//PRETAMOS APROBADOS POR CLIENTE 
	@GetMapping("PrestamosCliente/{id}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> listadoPretamoCliente(@PathVariable ("id") Integer idCliente) {
			try {
	            Map<String, List<Map<String, Object>>> detallesPrestamos = prestamoService.listadoPrestamosCliente(idCliente);
	            return ResponseEntity.ok(detallesPrestamos);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("No se pudo los préstamos aprobados", e);
	        }
	    }
	
	

	//Exception PrestamoNotFound
	@ExceptionHandler(PrestamoNoEncontradoException.class)
	public ResponseEntity<String> handlePrestamoNoEncontradoException(PrestamoNoEncontradoException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
		
		
		
	//ELIMINAR PRESTAMO
	@DeleteMapping(value = "DeletePrestamo/{id}")
	public ResponseEntity<Void> deletePrestamo(@PathVariable("id") Integer id) {
		prestamoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
		
		
	
}
