package com.sena.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImplement implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		// TODO Auto-generated method stub
		return ordenRepository.save(orden);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		// TODO Auto-generated method stub
		return ordenRepository.findById(id);
	}

	@Override
	public List<Orden> findAll() {
		// TODO Auto-generated method stub
		return ordenRepository.findAll();
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public String generarNumeroOrden() {
		// en el se incrementa el numero de la orden para luego pasarlo a string
		int numero = 0;
		// nos retorna el string con el numero secuencial de la orden
		String numeroConcatenado = "";
		// lista de ordenes
		List<Orden> ordenes = findAll();
		// lista de enteros para el incremento
		List<Integer> numeros = new ArrayList<Integer>();
		// funciones de java8
		// una variable anonima
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		// validación
		if (ordenes.isEmpty()) {
			numero = 1;
		} else {
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}

		if (numero < 10) {
			numeroConcatenado = "0000000000" + String.valueOf(numero);
		} else if (numero < 100) {
			numeroConcatenado = "000000000" + String.valueOf(numero);
		} else if (numero < 1000) {
			numeroConcatenado = "00000000" + String.valueOf(numero);
		}

		return numeroConcatenado;
	}

}
