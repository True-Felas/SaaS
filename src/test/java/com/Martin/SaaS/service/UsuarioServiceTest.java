package com.Martin.SaaS.service;

import com.Martin.SaaS.model.Usuario;
import com.Martin.SaaS.repository.PerfilRepository;
import com.Martin.SaaS.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private PerfilRepository perfilRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UsuarioService usuarioService;

	@Test
	void buscarPorEmail_DeberiaRetornarUsuario() {
		Usuario u = Usuario.builder().email("test@test.com").build();
		when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(u));

		Optional<Usuario> resultado = usuarioService.buscarPorEmail("test@test.com");

		assertTrue(resultado.isPresent());
		assertEquals("test@test.com", resultado.get().getEmail());
	}

	@Test
	void existeEmail_DeberiaRetornarTrue() {
		when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(true);

		boolean existe = usuarioService.existeEmail("test@test.com");

		assertTrue(existe);
	}
}
