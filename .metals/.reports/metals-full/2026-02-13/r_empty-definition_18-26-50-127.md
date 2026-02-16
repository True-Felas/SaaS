error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/UsuarioController.java:com/Martin/SaaS/security/JwtUtil#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/UsuarioController.java
empty definition using pc, found symbol in pc: com/Martin/SaaS/security/JwtUtil#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 340
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/UsuarioController.java
text:
```scala
package com.Martin.SaaS.controller;

import com.Martin.SaaS.dto.RegistroRequest;
import com.Martin.SaaS.dto.UsuarioDTO;
import com.Martin.SaaS.mapper.EntityMapper;
import com.Martin.SaaS.model.Usuario;
import com.Martin.SaaS.service.SuscripcionService;
import com.Martin.SaaS.service.UsuarioService;
import com.Martin.SaaS.security.@@JwtUtil;
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
    private final JwtUtil jwtUtil;

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

            String token = jwtUtil.generateToken(usuario.getId(), usuario.getRole() != null ? usuario.getRole().name() : "USER");
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token, "usuario", mapper.toUsuarioDTO(usuario)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint de autenticación simple: recibe email y password y devuelve el UsuarioDTO
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            Usuario usuario = usuarioService.autenticar(email, password);
            String token = jwtUtil.generateToken(usuario.getId(), usuario.getRole() != null ? usuario.getRole().name() : "USER");
            return ResponseEntity.ok(Map.of("token", token, "usuario", mapper.toUsuarioDTO(usuario)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene usuarios. Si el `requesterId` pertenece a un admin, devuelve todos.
     * Si pertenece a un usuario normal, devuelve solo su propio registro.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios(@RequestParam(required = false) Long requesterId,
                                                            @RequestHeader(value = "Authorization", required = false) String authorization) {
        // Si viene Authorization (Bearer token), extraer requesterId del token
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                var claims = jwtUtil.validateAndParse(token).getBody();
                requesterId = Long.parseLong(claims.getSubject());
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        if (requesterId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return usuarioService.buscarPorId(requesterId)
                .map(requester -> {
                    if (requester.getRole() == com.Martin.SaaS.model.enums.Role.ADMIN) {
                        List<UsuarioDTO> usuarios = usuarioService.listarTodos().stream()
                                .map(mapper::toUsuarioDTO)
                                .toList();
                        return ResponseEntity.ok(usuarios);
                    } else {
                        UsuarioDTO dto = mapper.toUsuarioDTO(requester);
                        return ResponseEntity.ok(List.of(dto));
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
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

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/Martin/SaaS/security/JwtUtil#