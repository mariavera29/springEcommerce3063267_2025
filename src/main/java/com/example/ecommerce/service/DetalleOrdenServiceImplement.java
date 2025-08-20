package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.DetalleOrden;
import com.example.ecommerce.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenServiceImplement implements IDetalleOrdenService {
@Autowired
 private IDetalleOrdenRepository detalleOrdenRepository;
 
	@Override
	public DetalleOrden save(DetalleOrden detalleorden) {
		// TODO Auto-generated method stub
		return detalleOrdenRepository.save(detalleorden);
	}
	

}
