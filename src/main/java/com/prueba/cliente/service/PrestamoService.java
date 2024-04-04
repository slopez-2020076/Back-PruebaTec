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
import com.prueba.cliente.model.Estado;
import com.prueba.cliente.model.Prestamo;
import com.prueba.cliente.repository.ClienteRepository;
import com.prueba.cliente.repository.EstadoRepository;
import com.prueba.cliente.repository.PrestamoRepository;




@Service
public class PrestamoService {

	@Autowired
	private PrestamoRepository prestamoRepository;
	
	
	@Autowired
	private ClienteRepository clienteRepository;

	
	@Autowired
	private EstadoRepository estadoRepository;;
	
	
	
	// LISTAR TODOS LOS PRESTAMOS
	public List<Prestamo> findAll() {
		return prestamoRepository.findAll();
	}
	
	
	
	
	//GUARDAR PRESTAMO
	public <S extends Prestamo> S savePrestamo(S prestamo) {
		
		Optional<Cliente> clientExist= clienteRepository.findById(prestamo.getCliente().getId());
		
		//System.out.println(clientExist);
		if (clientExist.isPresent() ) {
			
				Cliente client = clientExist.get();
				Optional<Prestamo> prestamoExist= prestamoRepository.findById(clientExist.get().getId());
				if (prestamoExist.isPresent() ) {
			        
			    	
			    	if (prestamo.getMonto() <= 0) {
				        throw new PrestamoNoEncontradoException("El monto del préstamo debe ser mayor a 0.");
				    }

				    prestamo.setSaldo(prestamo.getMonto());

				    LocalDate fechaInicial, fechaFinal;
				    try {
				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				        fechaInicial = LocalDate.parse(prestamo.getFecha_inicial(), formatter);
				        fechaFinal = LocalDate.parse(prestamo.getFecha_final(), formatter);
				    } catch (DateTimeParseException e) {
				        throw new PrestamoNoEncontradoException("Una de las fechas no está en un formato válido.");
				    }

				    LocalDate fechaActual = LocalDate.now();
				    if (fechaInicial.isBefore(fechaActual)) {
				        throw new PrestamoNoEncontradoException("La fecha inicial del préstamo debe ser mayor o igual a la fecha actual.");
				    }

				    if (fechaFinal.isBefore(fechaInicial)) {
				        throw new PrestamoNoEncontradoException("La fecha final del préstamo debe ser mayor a la fecha inicial.");
				    }

				    Optional<Estado> setEstado = estadoRepository.findById(2);
				    Estado estadoSet = setEstado.get();
				    prestamo.setEstado_id(estadoSet);;
				    
				    prestamo.setPagos(0);
				    
				    prestamo.setCliente(client);
				    
				    // GUARDA EL PRESTAMO EN LA BASE DE DATOS
				    return prestamoRepository.save(prestamo);
				    
			    	
			    }else {
			    	throw new PrestamoNoEncontradoException("Ya existe un préstamo con el mismo ID de cliente.");
			    }

			}else{
			throw new PrestamoNoEncontradoException("Usuario no Existe.");	
		}
	}

	
	
	
	
	
	//UPDATE ESTADO Y DESCRIPCION PRESTAMO
	public void actualizarEstado(Prestamo prestamoActualizado, Integer id) {
        // VALIDAR SI EL PRESTAMO EXISTE

		Optional<Prestamo> prestamoOpcional = prestamoRepository.findById(id);
		if (prestamoOpcional.isPresent()) {
	        
			Optional<Estado> estadoExiste = estadoRepository.findById(prestamoActualizado.getEstado_id().getId());
			if(estadoExiste.isPresent()) {
				Estado setEstado = estadoExiste.get();
				Prestamo prestamoExist = prestamoOpcional.get();
				
				// ACTUALIZAR LOS DATOS DEL PRESTAMO EXISTENTE
				
				prestamoExist.setEstado_id(setEstado);
				prestamoExist.setDescripcion(prestamoActualizado.getDescripcion());
				      
				        
				//GUARDAR LOS CAMBIOS EN LA BASE DE DATOS
				prestamoRepository.save(prestamoExist);
				
			}else {
				throw new PrestamoNoEncontradoException("Estado No Existe.");
			}

		}else {
			throw new PrestamoNoEncontradoException("Prestamo No Existe.");
		}
		
    }
	
	
	
	
	
	
	
	//CONSULTA SALDO DE UN PRESTAMO 
	public Map<String, Object> calculateSaldo(Integer id) {
	    // VALIDA SI EL PRESTAMO EXISTE
	    Optional<Prestamo> prestamoOptional = prestamoRepository.findById(id);  
	    
	    if (prestamoOptional.isPresent()) {
	        Prestamo prestamo = prestamoOptional.get();
	        
	        Optional<Estado> estadoOptional = estadoRepository.findById(prestamo.getEstado_id().getId());
	        Estado estado = estadoOptional.orElseThrow(() -> new PrestamoNoEncontradoException("Estado no encontrado"));
	        
	        if (estado.getNombre_estado().equals("Aprobado")) {
	            Map<String, Object> detallesPrestamo = new HashMap<>();
	            detallesPrestamo.put("Prestamo", prestamo.getId());
	            detallesPrestamo.put("Saldo", prestamo.getSaldo());
	            detallesPrestamo.put("Estado", estado.getNombre_estado());
	            return detallesPrestamo;
	        } else {
	            throw new PrestamoNoEncontradoException("Prestamo no esta Aprobado.");
	        }
	    } else {
	        throw new PrestamoNoEncontradoException("Prestamo No Existe.");
	    }
	}
	
	
	
	
	
	//CALCULA LOS SALDOS DE TODOS LOS PRESTAMOS APROBADOS 
	public Map<String, List<Map<String, Object>>> calculateSaldosAprobados() {
	    List<Prestamo> prestamos = prestamoRepository.findAll();  
	    Map<String, List<Map<String, Object>>> detallesPrestamos = new HashMap<>();
	    
	    for (Prestamo prestamo : prestamos) {
	        Optional<Estado> estadoOptional = estadoRepository.findById(prestamo.getEstado_id().getId());
	        Estado estado = estadoOptional.orElseThrow(() -> new PrestamoNoEncontradoException("Estado no encontrado"));
	        
	        if (estado.getNombre_estado().equals("Aprobado")) {
	            Map<String, Object> detallesPrestamo = new HashMap<>();
	            detallesPrestamo.put("Prestamo", prestamo.getId());
	            detallesPrestamo.put("Saldo", prestamo.getSaldo());
	            detallesPrestamo.put("Estado", estado.getNombre_estado());
	            
	            String clienteNombre = prestamo.getCliente().getNombre(); 
	            detallesPrestamo.put("Cliente", clienteNombre);
	            
	            String clienteApellido = prestamo.getCliente().getApellido(); 
	            detallesPrestamo.put("Apellido", clienteApellido);
	            
	            // AGREGA DETALLES DEL PRESTAMO
	            detallesPrestamos.computeIfAbsent("PrestamosAprobados", k -> new ArrayList<>()).add(detallesPrestamo);
	        }
	    }
	    
	    return detallesPrestamos;
	}
	
	
		
		
	//LISTA DE TODOS LOS PRESTAMOS APROBADOS POR CLIENTE
	public Map<String, List<Map<String, Object>>> ListadoPrestamosAprobadosCliente(Integer idCliente) {
		List<Prestamo> prestamos = prestamoRepository.findAllByClienteId(idCliente);  
		Map<String, List<Map<String, Object>>> detallesPrestamos = new HashMap<>();
		    
		for (Prestamo prestamo : prestamos) {
			Optional<Cliente> clienteOpcional = clienteRepository.findById(prestamo.getCliente().getId());
		        
		        
			if(clienteOpcional.isPresent()) {
				Cliente cliente = clienteOpcional.orElseThrow(() -> new PrestamoNoEncontradoException("Cliente no encontrado"));
				        
				Optional<Estado> estadoOptional = estadoRepository.findById(prestamo.getEstado_id().getId());
				Estado estado = estadoOptional.orElseThrow(() -> new PrestamoNoEncontradoException("Estado no encontrado"));
				       
				if (estado.getNombre_estado().equals("Aprobado") && cliente.getId() == prestamo.getCliente().getId()) {
						Map<String, Object> detallesPrestamo = new HashMap<>();
					    if(prestamo.getSaldo() == 0.0) {
					    	detallesPrestamo.put("Estado Pago", "Cancelado");
					    }else {
					    	detallesPrestamo.put("Estado Pago", "Pendiente");
					    }
					    	detallesPrestamo.put("Prestamo", prestamo);
					        // AGREGA DETALLES DEL PRESTAMO A LA LISTA
					        detallesPrestamos.computeIfAbsent("PrestamosAprobados", k -> new ArrayList<>()).add(detallesPrestamo);
				}
			}else {
				throw new PrestamoNoEncontradoException("Cliente No Existe.");
			}
		       
		}
			return detallesPrestamos;
	}
	
	
		
		
	//LISTA DE TODOS LOS PRESTAMOS POR CLIENTE
	public Map<String, List<Map<String, Object>>> listadoPrestamosCliente(Integer idCliente) {
			List<Prestamo> prestamos = prestamoRepository.findAllByClienteId(idCliente);  
		    Map<String, List<Map<String, Object>>> detallesPrestamos = new HashMap<>();
		    
		    for (Prestamo prestamo : prestamos) {
		        Optional<Cliente> clienteOpcional = clienteRepository.findById(prestamo.getCliente().getId());
		        
		        
		       if(clienteOpcional.isPresent()) {
		    	   Cliente cliente = clienteOpcional.orElseThrow(() -> new PrestamoNoEncontradoException("Cliente no encontrado"));
			        
		    	   if (cliente.getId() == prestamo.getCliente().getId()) {
			            Map<String, Object> detallesPrestamo = new HashMap<>();
			           
			            detallesPrestamo.put("Estado Prestamo", prestamo.getEstado_id().getNombre_estado());
			            detallesPrestamo.put("Prestamo", prestamo);
			            
			            
			            // AGREGA DETALLES DEL PRESTAMO A LA LISTA
			            detallesPrestamos.computeIfAbsent("Solicitudes Prestamos", k -> new ArrayList<>()).add(detallesPrestamo);
			        }
		       }else{
		    	   throw new PrestamoNoEncontradoException("Cliente No Existe.");
		       }
		       
		    }
		    
		    return detallesPrestamos;
		}
	
		
	
	
	
	
	//ELIMINAR PRESTAMO POR ID
	public void deleteById(Integer id) {
		if (prestamoRepository.existsById(id)) {
			prestamoRepository.deleteById(id);
		}else {
			throw new PrestamoNoEncontradoException("El Prestamo  no fue encontrado");
		}
	
	}
	
	

	@SuppressWarnings("serial")
	public class PrestamoNoEncontradoException extends RuntimeException {
	    public PrestamoNoEncontradoException(String mensaje) {
	        super(mensaje);
	    }
	}
	
	

}

	

