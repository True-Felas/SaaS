error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/AdminController.java:com/Martin/SaaS/model/enums/Role#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/AdminController.java
empty definition using pc, found symbol in pc: com/Martin/SaaS/model/enums/Role#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 198
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/AdminController.java
text:
```scala
package com.Martin.SaaS.controller;

import com.Martin.SaaS.dto.UsuarioDTO;
import com.Martin.SaaS.mapper.EntityMapper;
import com.Martin.SaaS.model.Usuario;
import com.Martin.SaaS.model.enums.@@Role;
import com.Martin.SaaS.service.AuditService;
import com.Martin.SaaS.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones exclusivas de ADMINISTRADOR.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

	private final AuditService auditService;
	private final UsuarioService usuarioService;
	private final com.Martin.SaaS.service.SuscripcionService suscripcionService;
	private final EntityMapper mapper;

	/**
	 * Endpoint para obtener el historial de auditoría de suscripciones. Requiere
	 * que el solicitante sea ADMIN.
	 */
	@GetMapping("/auditoria")
	public ResponseEntity<?> obtenerAuditoria(@RequestParam Long requesterId) {
		// Verificación de Rol
		if (!esAdmin(requesterId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", "Acceso denegado. Se requiere rol ADMIN."));
		}

		List<Map<String, Object>> historial = auditService.obtenerHistorialSuscripciones();
		return ResponseEntity.ok(historial);
	}

	/**
	 * Endpoint para listar TODOS los usuarios del sistema. Solo para ADMIN.
	 */
	@GetMapping("/usuarios")
	public ResponseEntity<?> listarTodosUsuarios(@RequestParam Long requesterId) {
		if (!esAdmin(requesterId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", "Acceso denegado. Se requiere rol ADMIN."));
		}

		List<UsuarioDTO> usuarios = usuarioService.listarTodos().stream().map(mapper::toUsuarioDTO).toList();
		return ResponseEntity.ok(usuarios);
	}

	/**
	 * Endpoint para obtener estadísticas globales. Solo para ADMIN.
	 */
	@GetMapping("/stats")
	public ResponseEntity<?> obtenerEstadisticas(@RequestParam Long requesterId) {
		if (!esAdmin(requesterId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", "Acceso denegado. Se requiere rol ADMIN."));
		}

		return ResponseEntity.ok(suscripcionService.obtenerStatsGlobales());
	}

	/**
	 * Endpoint para listar TODAS las suscripciones. Solo para ADMIN.
	 */
	@GetMapping("/suscripciones")
	public ResponseEntity<?> listarTodasSuscripciones(@RequestParam Long requesterId) {
		if (!esAdmin(requesterId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", "Acceso denegado. Se requiere rol ADMIN."));
		}

		return ResponseEntity
				.ok(suscripcionService.listarTodasSuscripciones().stream().map(mapper::toSuscripcionDTO).toList());
	}

	/**
	 * Método auxiliar para verificar rol de admin. En un sistema real esto se
	 * manejaría con Spring Security Filters / JWT.
	 */
	private boolean esAdmin(Long usuarioId) {
		return usuarioService.buscarPorId(usuarioId).map(u -> u.getRole() == Role.ADMIN).orElse(false);
	}
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/Martin/SaaS/model/enums/Role#