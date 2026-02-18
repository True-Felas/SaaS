error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/AdminController.java:com/Martin/SaaS/service/SecurityService#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/AdminController.java
empty definition using pc, found symbol in pc: com/Martin/SaaS/service/SecurityService#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 201
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/AdminController.java
text:
```scala
package com.Martin.SaaS.controller;

import com.Martin.SaaS.dto.UsuarioDTO;
import com.Martin.SaaS.mapper.EntityMapper;
import com.Martin.SaaS.service.AuditService;
import com.Martin.SaaS.service.@@SecurityService;
import com.Martin.SaaS.service.SuscripcionService;
import com.Martin.SaaS.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones exclusivas de ADMINISTRADOR.
 * Toda verificación de rol delegada a SecurityService.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

	private final AuditService auditService;
	private final UsuarioService usuarioService;
	private final SuscripcionService suscripcionService;
	private final EntityMapper mapper;
	private final SecurityService securityService;

	/** Historial de auditoría de suscripciones. Solo ADMIN. */
	@GetMapping("/auditoria")
	public ResponseEntity<?> obtenerAuditoria(@RequestParam Long requesterId) {
		try {
			securityService.requireAdmin(requesterId);
			List<Map<String, Object>> historial = auditService.obtenerHistorialSuscripciones();
			return ResponseEntity.ok(historial);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		}
	}

	/** Lista todos los usuarios del sistema. Solo ADMIN. */
	@GetMapping("/usuarios")
	public ResponseEntity<?> listarTodosUsuarios(@RequestParam Long requesterId) {
		try {
			securityService.requireAdmin(requesterId);
			List<UsuarioDTO> usuarios = usuarioService.listarTodos().stream().map(mapper::toUsuarioDTO).toList();
			return ResponseEntity.ok(usuarios);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		}
	}

	/** Estadísticas globales. Solo ADMIN. */
	@GetMapping("/stats")
	public ResponseEntity<?> obtenerEstadisticas(@RequestParam Long requesterId) {
		try {
			securityService.requireAdmin(requesterId);
			return ResponseEntity.ok(suscripcionService.obtenerStatsGlobales());
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		}
	}

	/** Lista todas las suscripciones. Solo ADMIN. */
	@GetMapping("/suscripciones")
	public ResponseEntity<?> listarTodasSuscripciones(@RequestParam Long requesterId) {
		try {
			securityService.requireAdmin(requesterId);
			return ResponseEntity.ok(
					suscripcionService.listarTodasSuscripciones().stream().map(mapper::toSuscripcionDTO).toList());
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		}
	}
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/Martin/SaaS/service/SecurityService#