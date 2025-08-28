package com.example.ecommerce.controller;


import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.model.Producto;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.service.IProductoService;
import com.example.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	//instance LOGGER- 
	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductoController.class);
	@Autowired
	private IProductoService productoservice;
	
	
	@Autowired
	private UploadFileService upload;
	//Method tables listar productos
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoservice.findAll());
		return "productos/show";
	}
	//Create formulario, metodo de redireccionamiento a el form de creacion de productoS
	@GetMapping("/create")
	public String create() {
	return "productos/create";
	}
	
	//metodo de creacionn de productos
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file)  throws IOException {
		LOGGER.info("Este es el objeto del producto a guardar en la DB{}", producto );
		Usuario u = new Usuario(1, "", "", "", "", "", "", "", "") ;
		producto.setUsuario(u); 
		//validacion imagen deñ producto
		if(producto.getId() == null) {
			String nombreImagen = upload.saveImages(file, producto.getNombre());
			producto.setImagen(nombreImagen);
		}
		productoservice.save(producto);
		return "redirect:/productos";
	}
	
	//metodo para el formulario de edicion de productos
	
	@GetMapping("/edit/{id}")
	public String edit (@PathVariable Integer id, Model model) {
		Producto p = new Producto();
		
	//nos retorna la busqueda de un objeto de tipo producto con el id
		
		Optional <Producto> op = productoservice.get(id);
		p= op.get();
		LOGGER.warn("Busqueda de producto por id {}", p );
		model.addAttribute("producto",p);
		return "productos/edit";
	}
	
	//metodo de actualizacion de data
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file)  throws IOException {
	LOGGER.info("Este es el objeto del producto a actualizar en la DB{}", producto );
	Producto p = new Producto();
	p = productoservice.get(producto.getId()).get();
	if (file.isEmpty()) {
		producto.setImagen(p.getImagen());
		
	}else {
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImagen(p.getImagen());
		}
		String nombreImagen = upload.saveImages(file, p.getNombre());
		producto.setImagen(nombreImagen);
		
	}
	producto.setUsuario(p.getUsuario()); 
	productoservice.update(producto);
	return "redirect:/productos";
		
	}
// metodo para eliminar con id un producto
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		Producto p = new Producto();
		p = productoservice.get(id).get();
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImagen(p.getImagen());
			
		}
	productoservice.delete(id);
	return"redirect:/productos";
}



}
