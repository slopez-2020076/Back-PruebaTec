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

import com.prueba.cliente.model.Cliente;
import com.prueba.cliente.service.ClienteService;
import com.prueba.cliente.service.ClienteService.UsuarioNoEncontradoException;

@RestController
@RequestMapping("/Cliente/")
public class ClienteRest {
	
	@Autowired
	private ClienteService clienteService;
	
	
	//LISTAR TODOS LOS CLIENTES
	@GetMapping
	private ResponseEntity<List<Cliente>> getAllClientes(){
		return ResponseEntity.ok(clienteService.findAll());
	}
	
	
	//GUARDAR CLIENTE
	@PostMapping("GuardarCliente")
	private Cliente saveCliente(@RequestBody Cliente cliente) {
	    try {
	        Cliente clienteGuardado = clienteService.saveCliente(cliente);
	        return clienteGuardado;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("No se pudo guardar el cliente", e);
	    }
	}
	
	
	//ACTUALIZAR CLIENTE
	@PutMapping("EditarCliente/{id}")
	public ResponseEntity<String> editarCliente(@RequestBody Cliente clienteActualizado, @PathVariable ("id") Integer id) {
        try {
            clienteService.editarCliente(clienteActualizado, id);
            return ResponseEntity.ok("Cliente actualizado correctamente");
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
	
	

    //Exception ClientNotFound
	@ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<String> handleUsuarioNoEncontradoException(UsuarioNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
	
	
	//ELIMINAR  CLIENTE POR ID
	@DeleteMapping (value = "DeleteCliente/{id}")
	private ResponseEntity<Boolean> deletePersona (@PathVariable ("id") Integer id){
		try {
			return ResponseEntity.ok(clienteService.deleteById(id));
		}catch(Exception e){
			e.printStackTrace();
	        throw new RuntimeException("No se pudo eliminar el cliente", e);
		}
	}
	
	
	
	
	//ELIMINAR  CLIENTE POR ID
		@DeleteMapping (value = "DeleteClientePrestamos/{id}")
		public ResponseEntity<Map<String, List<Map<String, Object>>>> eliminarClientePerstamos(@PathVariable("id") Integer idCliente) {
	        try {
	            Map<String, List<Map<String, Object>>> detallesPrestamos = clienteService.EliminarClientePrestamos(idCliente);
	            return ResponseEntity.ok(detallesPrestamos);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("No se pudo eliminar el cliente y los prestamos.", e);
	        }
	    }

}
	
	

