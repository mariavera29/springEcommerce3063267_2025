package com.sena.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;
import com.sena.ecommerce.service.UploadFileService;

@RestController
@RequestMapping("/apiproductos")
public class apiProductoController {
	@Autowired
	private IProductoService productoservice;

	@Autowired
	private UploadFileService upload;

	@Autowired
	private IUsuarioService usuarioservice;

	// end point GET para obtener todos los productos
	@GetMapping("/api")
	public List<Producto> getAllProducts() {
		return productoservice.findAll();
	}

	// end point GET para obtener un producto por ID
	@GetMapping("/api/{id}")
	public ResponseEntity<Producto> getProductById(@PathVariable Integer id) {
		Optional<Producto> producto = productoservice.get(id);
		return producto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

	}

	// end point POST para crear un producto por ID
	@PostMapping("/api")
	public ResponseEntity<Producto> creategetProduct(@RequestBody Producto producto) {
		Usuario u = usuarioservice.findById(1).get();
		producto.setUsuario(u);
		if (producto.getImagen() == null)
			producto.setImagen("default.jpg");
		Producto sp = productoservice.save(producto);
		return ResponseEntity.status(HttpStatus.CREATED).body(sp);

	}

	// end point DELETE para eliminar un producto por ID
	@DeleteMapping("/api/{id}")
	public ResponseEntity<Producto> deleteProduct(@PathVariable Integer id) {

		Optional<Producto> p = productoservice.get(id);
		if (!p.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Producto prod = p.get();
		if (!prod.getImagen().equals("default.jpg")) {
		}
		productoservice.delete(id);
		return ResponseEntity.ok().build();
	}

}
