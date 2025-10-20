package com.sena.ecommerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sena.ecommerce.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailServiceImplment implements UserDetailsService {
@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	HttpSession session;

	private Logger log = LoggerFactory.getLogger(UserDetailServiceImplment.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.warn("Este es el username");
		Optional<Usuario> ou = usuarioService.findByEmail(username);
		
		if (ou.isPresent()) {
			log.warn("Esto es el ID del usuario: {}", ou.get().getId());
			session.setAttribute("idUsuario", ou.get().getId());
			Usuario u = ou.get();
			return User.builder().username(u.getNombre()).password(u.getPassword()).roles(u.getTipo()).build();
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		
	
	}
	
	public String encodePass(String rowPass) {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		return pe.encode(rowPass);
	}

}
