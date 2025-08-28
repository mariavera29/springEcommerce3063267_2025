package com.example.ecommerce.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce.model.Producto;
import com.example.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	// instance LOGGER-
	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductoController.class);
	@Autowired
	private IProductoService productoservice;

	@GetMapping("")
	public String home(Model model) {// solicitud del mapeo al directorio administrador
		List<Producto> productos = productoservice.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";

	}

}
