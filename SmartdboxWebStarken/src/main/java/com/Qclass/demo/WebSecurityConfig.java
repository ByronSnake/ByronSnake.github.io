package com.Qclass.demo;

import java.security.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


//@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/actuator/**").hasRole("ADMIN")
						.anyRequest()
						.authenticated()
						).httpBasic(Customizer.withDefaults())				 
				.cors();
		return http.build();
	}
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		UserDetails React = User.builder()
				.username("React")
				.password(passwordEncoder().encode("Re.@ct2023"))
				.roles("FRONT")
				.build();
		
		UserDetails Backend = User.builder()
				.username("Backend")
				.password(passwordEncoder().encode("B@ck.3nd2023"))
				.roles("DASHBOARD")
				.build();
		
		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder().encode("@dm.1n2023"))
				.roles("ADMIN")
				.build();
		
		
		return new InMemoryUserDetailsManager(React,Backend,admin);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout((logout) -> logout.permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("{noop}password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
	*/
	
}
