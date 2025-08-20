package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdministradorController  {
	
	@GetMapping("")
	public String home() {//solicitud del mapeo al directorio administrador
		return "administrador/home";
	
	}

}
