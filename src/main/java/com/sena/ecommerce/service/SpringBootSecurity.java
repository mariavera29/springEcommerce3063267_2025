package com.sena.ecommerce.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//clase interceptora
@Configuration
public class SpringBootSecurity {

	private UserDetailsService userDetailsService;

	// validacion de usuario que sea el correcto
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
	}

	@SuppressWarnings({ "deprecation", "removal" })
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(requests -> requests.requestMatchers("/administrdor/").hasRole("ADMIN")
				.requestMatchers("/productos/").hasRole("ADMIN"))
				// csrf evita sql inyection
				.csrf(csrf -> csrf.disable())
				.formLogin(l -> l.loginPage("/usuario/login").permitAll().defaultSuccessUrl("/usuario/acceder"))
				.logout(lg -> lg.permitAll());
		;
		return http.build();
	}

	@Bean
	BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
}
