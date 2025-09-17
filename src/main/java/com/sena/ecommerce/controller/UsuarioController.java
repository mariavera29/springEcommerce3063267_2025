package com.sena.ecommerce.controller;

import java.util.List;
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

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@GetMapping("/registro")
	public String createUser() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario, Model model) {
		LOGGER.warn("Usuario a registrar: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		LOGGER.warn("Accesos: {}", usuario);
		// acceder a la DB para validar
		Optional<Usuario> userEmail = usuarioService.findByEmail(usuario.getEmail());
		// LOGGER.warn("Usuario obtenido de la DB: {}", userEmail.get());
		// condicion temporal sin spring security
		if (userEmail.isPresent()) {
			LOGGER.warn("Usuario obtenido de la DB: {}", userEmail.get());
			// id del usuario encontrado
			session.setAttribute("idUsuario", userEmail.get().getId());
			// validacion tipo de usuario
			if (userEmail.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			LOGGER.warn("Usuario no existe en DB");
		}
		return "redirect:/";
	}

	// metodo para obtener o listar las ordenes de compras
	@GetMapping("/compras")
	public String compras(Model model, HttpSession session) {
		// sesion de usuario o el idUsuario objeto de session
		model.addAttribute("session", session.getAttribute("idUsuario"));
		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(u);
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}

	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, Model model, HttpSession session) {
		// sesion de usuario o el idUsuario objeto de session
		model.addAttribute("session", session.getAttribute("idUsuario"));
		LOGGER.warn("id de la orden: {}", id);
		Optional<Orden> orden = ordenService.findById(id);
		model.addAttribute("detalles",orden.get().getDetalle());
		return "usuario/detallecompra";
	}
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idUsuario");
		return"/redirect:/";
	}
}
