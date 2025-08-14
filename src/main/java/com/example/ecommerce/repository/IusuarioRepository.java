package com.example.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ecommerce.model.Usuario;


@Repository
public interface IusuarioRepository extends JpaRepository< Usuario, Integer>{
	
	Optional<Usuario> findByEmail(String email); 
	
	Optional<Usuario> findByUsername(String username); 

}
