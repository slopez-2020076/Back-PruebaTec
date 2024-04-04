package com.prueba.cliente.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.prueba.cliente.model.Estado;
import com.prueba.cliente.repository.EstadoRepository;
@Service
public class EstadoService {

	
	@Autowired
	private EstadoRepository estadoRepository;

	//LISTAR TODOS LOS ESTADOS DE PRESTAMO 
	public List<Estado> findAll() {
		return estadoRepository.findAll();
	}
		
	
	//ELIMINAR ESTADO DE PRESTAMO
	public void deleteById(Integer id) {
		
		if (estadoRepository.existsById(id)) {
			estadoRepository.deleteById(id);
		}else {
			throw new EstadoNoEncontradoException("El Estado  no fue encontrado");
		}
	
	}
	
	
	@SuppressWarnings("serial")
	public class EstadoNoEncontradoException extends RuntimeException {
	    public EstadoNoEncontradoException(String mensaje) {
	        super(mensaje);
	    }
	}
	
	
	
}
