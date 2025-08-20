package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.repository.IusuarioRepository;
@Service
public class UsuarioServiceImplement implements IUsuarioService {
	
	//instancia de objeto
	@Autowired
	 private IusuarioRepository usuariorepository;
	
	
	 
	@Override
	public Usuario save(Usuario usuario) {
		// TODO Auto-generated method stub
		return  usuariorepository.save(usuario);
		
		
	}
	@Override
	public Optional<Usuario> get(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public void update(Usuario usuario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Usuario> findById(Integer id) {
		// TODO Auto-generated method stub
		return usuariorepository.findById(id);
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {
		// TODO Auto-generated method stub
		return usuariorepository.findByEmail(email);
	}

	@Override
	public List<Usuario> findAll() {
		// TODO Auto-generated method stub
		return usuariorepository.findAll();
	}

}
