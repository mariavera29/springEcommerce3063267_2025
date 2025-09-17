package com.sena.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.sena.ecommerce.model.DetalleOrden;
import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IDetalleOrdenService;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeUserController {

	// instancia del LOGGER
	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(HomeUserController.class);

	// instancia de productoService
	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// lista de detalles de la orden para guardar en la DB
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	// objeto que almacena los datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		LOGGER.warn("sesion de usuario: {}", session.getAttribute("idUsuario"));
		model.addAttribute("productos", productoService.findAll());
		// variable de sesion
		model.addAttribute("session", session.getAttribute("idUsuario"));
		return "usuario/home";
	}

	// metodo que carga el producto del usuario con el id producto
	@GetMapping("productohome/{id}")
	public String productohome(@PathVariable Integer id, Model model) {
		LOGGER.warn("Id producto enviado como parametro {}", id);
		// variable de la clase producto
		Producto p = new Producto();
		Optional<Producto> op = productoService.get(id);
		// pasar el producto
		p = op.get();
		model.addAttribute("producto", p);
		return "usuario/productohome";
	}

	// metodo para enviar del boton del formulario de producto home al carrito de
	// compras
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Double cantidad, Model model) {
		DetalleOrden detaorden = new DetalleOrden();
		Producto p = new Producto();
		// variable de tipo double que siempre que se ingrese en el metodo se inicializa
		// en cero despues de cada compra
		double sumaTotal = 0;
		Optional<Producto> op = productoService.get(id);
		LOGGER.warn("Producto añadido: {}", op.get());
		LOGGER.warn("Cantidad añadida: {}", cantidad);
		p = op.get();
		detaorden.setCantidad(cantidad);
		detaorden.setPrecio(p.getPrecio());
		detaorden.setNombre(p.getNombre());
		detaorden.setTotal(p.getPrecio() * cantidad);
		detaorden.setProducto(p);
		// validacion para evitar duplicados de productos
		Integer idProducto = p.getId();
		// funcion lamda stream y funcion anonima con predicado anyMatch
		boolean insertado = detalles.stream().anyMatch(prod -> prod.getProducto().getId() == idProducto);
		// si no es true añade el producto a la lista
		if (!insertado) {
			detalles.add(detaorden);
		}
		// suma de totales de la lista que el usuario añade al carrito
		// funciones de java8 lamda strem
		// funcion anonima de java 8 dt
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		// pasar variables a la vista
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}

	// metodo para quitar productos del carrito de compras
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		// lista nueva de productos del carrito
		List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
		// quitar un objeto de la lista de detalleOrden
		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNuevas.add(detalleOrden);
			}
		}
		// cargar nueva lista con los porductos restantes del carrito
		detalles = ordenesNuevas;
		// recalcular los totales de la lista
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		// pasar variables a la vista
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}

	// metodo para redirigir al carrito de compras sin productos
	@GetMapping("/getCart")
	public String getCart(Model model) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}

	// metodo para redirigir a la vista del resumen de la orden
	@GetMapping("/orden")
	public String orden(Model model, HttpSession session) {
		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", u);
		return "usuario/resumenorden";
	}

	// metodo que genera la orden y los detalles de la orden
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechacreacion = new Date();
		orden.setFechacreacion(fechacreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		orden.setUsuario(u);
		ordenService.save(orden);
		// guardar detalles de la orden
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
			// descuento de cantidad de produto comprada del stock del producto
			Producto p = dt.getProducto();
			int cantComprada = dt.getCantidad().intValue();// conversion double a int
			if (p.getCantidad() >= cantComprada) {
				p.setCantidad(p.getCantidad() - cantComprada);
				productoService.update(p);
			} else {
				throw new IllegalStateException("Stock insuficiente para el producto: " + p.getNombre());
			}

		}
		// limpiar los valores que no se añadan a la siguiente orden o a la orden recien
		// guardada
		orden = new Orden();
		detalles.clear();
		return "redirect:/";
	}

	// metodo post para buscar productos de la vista principal o home de usuarios
	@PostMapping("/search")
	public String searchProducto(@RequestParam String nombre, Model model) {
		LOGGER.warn("nombre del producto: {}", nombre);
		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().toUpperCase().contains(nombre.toUpperCase())
						|| p.getDescripcion().toUpperCase().contains(nombre.toUpperCase()))
				.collect(Collectors.toList());
		model.addAttribute("productos", productos);
		return "usuario/home";
	}

}
