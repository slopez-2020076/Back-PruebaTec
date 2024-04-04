package com.prueba.cliente.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.cliente.model.Estado;
import com.prueba.cliente.model.MetodoPago;
import com.prueba.cliente.model.Pago;
import com.prueba.cliente.model.Prestamo;
import com.prueba.cliente.repository.EstadoRepository;
import com.prueba.cliente.repository.MetodoPagoRepository;
import com.prueba.cliente.repository.PagoRepository;
import com.prueba.cliente.repository.PrestamoRepository;

@Service
public class PagoService {
	
	@Autowired
	private PagoRepository pagoRepository;
	
	@Autowired
	private PrestamoRepository prestamoRepository;
	
	@Autowired
	private MetodoPagoRepository metodoPagoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	
	//LISTAR TODOS LOS PAGOS
	public List<Pago> findAll() {
		return pagoRepository.findAll();
	}
	

	
	
	
//GUARDAR PAGO PRESTAMO
public <S extends Pago> S guardarPago(S pago) {
		
		Optional<Prestamo> prestamoExist= prestamoRepository.findById(pago.getNumero_prestamo().getId());
		
		//System.out.println(clientExist);
		if (prestamoExist.isPresent() ) {
				Prestamo prestamo = prestamoExist.get();
				Optional<MetodoPago> metodoOpcional = metodoPagoRepository.findById(pago.getNombre_metodo().getId());
				MetodoPago metodoPago = metodoOpcional.get();
				
				Optional<Estado> estadoOpcional = estadoRepository.findById(prestamo.getEstado_id().getId());
				Estado estado = estadoOpcional.get();
				
				if(estado.getNombre_estado().equals("Aprobado")) {
					
					if (metodoPago.getId() != 1) {
				    	throw new PaymentNoEncontradoException("Solo se pueden realizar pagos en efectivo.");
					}
				        
				    	//PAGO MAYOR A 0
				    	if (pago.getMontoPago() <= 0 || pago.getMontoPago() > prestamo.getSaldo()) {
					        throw new PaymentNoEncontradoException("El monto del préstamo debe ser mayor a 0 y menor al saldo del prestamo.");
					    }

				    	//ACTUALIZAR SALDO PRESTAMO
				        Double saldoActualizado; 
				       	saldoActualizado = prestamo.getSaldo() -  pago.getMontoPago(); 
				       	prestamo.setSaldo(saldoActualizado);
				        	
				       	
				       	//ACTUALIZANDO CANTIDAD DE PAGOS
			        	Integer updatePayments;
			       		updatePayments = prestamo.getPagos() + 1;
				       	prestamo.setPagos(updatePayments);

				       	
				       	//ACTUALIZANDO PRESTAMO
				       	prestamoRepository.save(prestamo);
				        
				       	
				       	pago.setNumero_prestamo(prestamo);
				       	pago.setNombre_metodo(metodoPago);
				       	
				       	
				       	//OBTENCION FECHA ACTUAL
					    LocalDate fechaNow = LocalDate.now();
					    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					    String fecha = fechaNow.format(formatter);
					    pago.setFechaPago(fecha);

					    // GUARDA EL PAGO EN LA BASE DE DATOS
					    return pagoRepository.save(pago);
					    
				    	
				    
				}else {
					throw new PaymentNoEncontradoException("El prestamo no esta Aprobado.");
				}
				

		}else{
			throw new PaymentNoEncontradoException("Prestamo no Existe.");	
		}
	}




	//ELIMINAR PAGO POR ID
	public void deleteById(Integer id) {
		if (pagoRepository.existsById(id)) {
			pagoRepository.deleteById(id);
		}
			
	}
	

	//Exception Payment not Found 
	@SuppressWarnings("serial")
	public class PaymentNoEncontradoException extends RuntimeException {
	    public PaymentNoEncontradoException(String mensaje) {
	        super(mensaje);
	    }
	}
	



	
}
