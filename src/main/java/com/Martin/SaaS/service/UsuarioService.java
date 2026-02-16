package com.Martin.SaaS.service;

import com.Martin.SaaS.model.Perfil;
import com.Martin.SaaS.model.Usuario;
import com.Martin.SaaS.repository.PerfilRepository;
import com.Martin.SaaS.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

// Servicio para gestión de usuarios.
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PerfilRepository perfilRepository;
	private final PasswordEncoder passwordEncoder;

	// Registra un nuevo usuario.
	public Usuario registrarUsuario(String email, String password, String nombre, String apellido) {
		// Verificar si el email ya existe
		if (usuarioRepository.existsByEmail(email)) {
			throw new RuntimeException("El email ya está registrado");
		}

		Usuario usuario = Usuario.builder().email(email).password(passwordEncoder.encode(password)).nombre(nombre)
				.apellido(apellido).activo(true).emailVerificado(false).build();

		usuario = usuarioRepository.save(usuario);

		// Crear perfil vacío
		Perfil perfil = Perfil.builder().usuario(usuario).build();
		perfilRepository.save(perfil);
		usuario.setPerfil(perfil);

		log.info("Usuario registrado: {}", email);
		return usuario;
	}

	// Busca un usuario por email.
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	// Verifica si un email ya está registrado.
	@Transactional(readOnly = true)
	public boolean existeEmail(String email) {
		return usuarioRepository.existsByEmail(email);
	}

	// Busca un usuario por ID.
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorId(Long id) {
		return usuarioRepository.findById(id);
	}

	// Lista todos los usuarios activos.
	@Transactional(readOnly = true)
	public List<Usuario> listarUsuariosActivos() {
		return usuarioRepository.findByActivoTrue();
	}

	// Lista todos los usuarios.
	@Transactional(readOnly = true)
	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	// Lista todos los usuarios ordenados por fecha de registro descendente.
	@Transactional(readOnly = true)
	public List<Usuario> listarTodosOrdenadosPorFechaRegistroDesc() {
		return usuarioRepository.findAllByOrderByFechaRegistroDesc();
	}

	// Actualiza los datos de un usuario.
	public Usuario actualizarUsuario(Long id, String nombre, String apellido) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		usuario.setNombre(nombre);
		usuario.setApellido(apellido);

		return usuarioRepository.save(usuario);
	}

	// Desactiva un usuario.
	public Usuario desactivarUsuario(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		usuario.setActivo(false);
		return usuarioRepository.save(usuario);
	}

	// Autentica a un usuario por email y contraseña.
	@Transactional(readOnly = true)
	public Usuario autenticar(String email, String password) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

		if (usuario.getPassword() == null || !passwordEncoder.matches(password, usuario.getPassword())) {
			throw new RuntimeException("Credenciales inválidas");
		}

		return usuario;
	}
}
