package com.prueba.cliente.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.cliente.model.Cliente;
import com.prueba.cliente.model.Prestamo;
import com.prueba.cliente.repository.ClienteRepository;
import com.prueba.cliente.repository.PrestamoRepository;

@Service
public class ClienteService {

	
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PrestamoRepository prestamoRepository;
	
	//LISTAR TODSOS LOS CLIENTES
	public List<Cliente> findAll() {		
		return clienteRepository.findAll();
	}
		
	
	
	//GUARDAR CLIENTE
		public <S extends Cliente> S saveCliente(S cliente) {
		    
			String dpi = (cliente.getDpi());
			Cliente clienteExist = clienteRepository.findFirstByDpi(dpi);


		    //VALIDAR DPI 
		    if (clienteExist != null ) {
		        throw new UsuarioNoEncontradoException("Ya existe un usuario con el DPI ingresado.");
		    }

		    //VALIDAR FECHA DE NACIMIENTO
		    LocalDate fechaNacimiento;
		    try {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        fechaNacimiento = LocalDate.parse(cliente.getFecha_nacimiento(), formatter);
		    } catch (DateTimeParseException e) {
		        throw new UsuarioNoEncontradoException("La fecha de nacimiento no está en un formato válido.");
		    }
		    
		    
		    LocalDate fechaMinima = LocalDate.now().minusYears(18);
		    if (fechaNacimiento.isAfter(fechaMinima)) {
		        throw new UsuarioNoEncontradoException("El cliente debe tener al menos 18 años.");
		    }

		    //GUARDADO
		    return clienteRepository.save(cliente);
		}
	

	
	//ACTUALIZAR CLIENTE
	public void editarCliente(Cliente clienteActualizado, Integer id) {
        //VALIDAR CLKIENTE EXISTENTE

		Optional<Cliente> clienteOptional = clienteRepository.findById(id);
		if (clienteOptional.isPresent()) {
	        Cliente clienteExistente = clienteOptional.get();
	        
	    
	        Cliente clienteExist = clienteRepository.findFirstByDpi(clienteActualizado.getDpi());
			if (clienteExist != null) { throw new UsuarioNoEncontradoException("Ya existe un cliente con el DPI ingresado."); 
			
			}
				//VALIDA FECHA NACIMIENTO
		        LocalDate fechaNacimiento;
		        try {
		            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		            fechaNacimiento = LocalDate.parse(clienteActualizado.getFecha_nacimiento(), formatter);
		        } catch (DateTimeParseException e) {
		            throw new UsuarioNoEncontradoException("La fecha de nacimiento no está en un formato válido.");
		        }

		        LocalDate fechaMinima = LocalDate.now().minusYears(18);
		        if (fechaNacimiento.isAfter(fechaMinima)) {
		            throw new UsuarioNoEncontradoException("El cliente debe tener al menos 18 años.");
		        }
		        
		        
		        
		     //ACTUALIZAR DATOS DEL CLIENTE EXISTENTE
		        clienteExistente.setNombre(clienteActualizado.getNombre());
		        clienteExistente.setApellido(clienteActualizado.getApellido());
		        clienteExistente.setDpi(clienteActualizado.getDpi());
		        clienteExistente.setFecha_nacimiento(clienteActualizado.getFecha_nacimiento());
		        clienteExistente.setDireccion(clienteActualizado.getDireccion()); 
		        clienteExistente.setCorreo_electronico(clienteActualizado.getCorreo_electronico());
		        clienteExistente.setTelefono(clienteActualizado.getTelefono());
		        clienteExistente.setEstado(clienteActualizado.isEstado());
		        
		        //GUARDAR CAMBIOS EN LA BASE DE DATOS
		        clienteRepository.save(clienteExistente);
			
	        
		}else {
			throw new UsuarioNoEncontradoException("Usuario No Existe.");
		}
		
    }
	
	
	
	
	//ELIMINAR CLIENTE POR ID
	public Boolean deleteById(Integer id) {
		if (clienteRepository.existsById(id)) {
			clienteRepository.deleteById(id);
			return !clienteRepository.existsById(id);
		}
		return false;
	}
	
	
		
	

	
	
	//ELIMINAR CLIENTE Y SUS PRESTAMOS
	public Map<String, List<Map<String, Object>>> EliminarClientePrestamos(Integer idCliente) {
        List<Prestamo> prestamos = prestamoRepository.findAllByClienteId(idCliente);
        Map<String, List<Map<String, Object>>> detallesPrestamos = new HashMap<>();
        
        System.out.println(prestamos);
        if (prestamos.isEmpty()) {

        	throw new UsuarioNoEncontradoException("El cliente no posee prestamos asociados o Cliente no existe.");
        } else {
        	
        	for (Prestamo prestamo : prestamos) {
                Map<String, Object> detallesPrestamo = new HashMap<>();
                prestamoRepository.deleteById(prestamo.getId());
                
                detallesPrestamo.put("Id", prestamo.getId());
                detallesPrestamo.put("Monto", prestamo.getMonto());
                detallesPrestamo.put("Descripcion", prestamo.getDescripcion());
                detallesPrestamo.put("Estado", prestamo.getEstado_id().getNombre_estado());
                detallesPrestamo.put("Nombre", prestamo.getCliente().getNombre());
                detallesPrestamo.put("Nombre", prestamo.getCliente().getApellido());

                detallesPrestamos.computeIfAbsent("Prestamos_eliminados", k -> new ArrayList<>()).add(detallesPrestamo);
            }

            clienteRepository.deleteById(idCliente);
            
        }

        clienteRepository.deleteById(idCliente);

        return detallesPrestamos;
    }
		
		
		
	
	
	@SuppressWarnings("serial")
	public class UsuarioNoEncontradoException extends RuntimeException {
	    public UsuarioNoEncontradoException(String mensaje) {
	        super(mensaje);
	    }
	}
	
		
}
