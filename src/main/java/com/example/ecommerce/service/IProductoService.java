package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.example.ecommerce.model.Producto;

public interface IProductoService {
	
	public Producto save(Producto producto);
	public Optional<Producto> get (Integer id);
	public void update(Producto producto);
	public void delete(Integer id);
	
	public List <Producto> findAll();
	List<Producto> finAll();

}
