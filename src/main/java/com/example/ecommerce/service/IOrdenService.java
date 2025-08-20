package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.example.ecommerce.model.Orden;
import com.example.ecommerce.model.Usuario;

public interface IOrdenService {
	
	public Orden save(Orden orden);
	public Optional<Orden > get (Integer id);
	public List <Orden> findAll();
	public List <Orden> findByUsuario(Usuario usuario );
	public String generarNumeroOrden();
	Optional<Orden> findbyId(Integer id);
}
