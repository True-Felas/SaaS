package com.Martin.SaaS.controller;

import com.Martin.SaaS.dto.RegistroRequest;
import com.Martin.SaaS.dto.UsuarioDTO;
import com.Martin.SaaS.mapper.EntityMapper;
import com.Martin.SaaS.model.Usuario;
import com.Martin.SaaS.service.SuscripcionService;
import com.Martin.SaaS.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SuscripcionService suscripcionService;
    private final EntityMapper mapper;

    /**
     * Registra un nuevo usuario con opción de seleccionar plan.
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            Usuario usuario = usuarioService.registrarUsuario(
                    request.getEmail(),
                    request.getPassword(),
                    request.getNombre(),
                    request.getApellido()
            );

            // Si seleccionó un plan, crear suscripción
            if (request.getPlanId() != null) {
                suscripcionService.crearSuscripcion(usuario.getId(), request.getPlanId());
                usuario = usuarioService.buscarPorId(usuario.getId()).orElse(usuario);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toUsuarioDTO(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos().stream()
                .map(mapper::toUsuarioDTO)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(mapper::toUsuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un usuario por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email)
                .map(mapper::toUsuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
