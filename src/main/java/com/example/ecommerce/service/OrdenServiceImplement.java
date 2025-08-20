package com.example.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.Orden;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImplement implements IOrdenService{
	
	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		// TODO Auto-generated method stub
		return ordenRepository.save(orden);
	}

	@Override
	public Optional<Orden> findbyId(Integer id) {
		// TODO Auto-generated method stub
		return ordenRepository.findById(id);
	}

	@Override
	public List<Orden> findAll() {
		// TODO Auto-generated method stub
		return ordenRepository.findAll();
	}



	@Override
	public String generarNumeroOrden() {
		int numero = 0;
		
		String numeroConcatenado = "";
		
		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<Integer>();
		
		//funciones de java8
		//una variable anonima
		
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		//validación
		
		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}
		if (numero < 10) {
			numeroConcatenado = "T000000000"+ String.valueOf(numero);
		}else if (numero < 100) {
			numeroConcatenado = "T00000000"+ String.valueOf(numero);
		}else if (numero < 1000) {
			numeroConcatenado = "T0000000"+ String.valueOf(numero);
		}
		
		
		return numeroConcatenado;
	}

	@Override
	public Optional<Orden> get(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return ordenRepository.findByUsuario(usuario);
	}


	

}