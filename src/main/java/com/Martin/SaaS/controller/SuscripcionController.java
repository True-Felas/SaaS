package com.Martin.SaaS.controller;

import com.Martin.SaaS.dto.CambiarPlanRequest;
import com.Martin.SaaS.dto.FacturaDTO;
import com.Martin.SaaS.dto.SuscripcionDTO;
import com.Martin.SaaS.mapper.EntityMapper;
import com.Martin.SaaS.model.Factura;
import com.Martin.SaaS.model.Suscripcion;
import com.Martin.SaaS.service.SecurityService;
import com.Martin.SaaS.service.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

// Controlador REST para gestión de suscripciones.
@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuscripcionController {

	private final SuscripcionService suscripcionService;
	private final EntityMapper mapper;
	private final SecurityService securityService;

	// Crea una nueva suscripción para un usuario.
	@PostMapping
	public ResponseEntity<?> crearSuscripcion(@RequestBody Map<String, Object> request,
			@RequestParam(required = false) Long requesterId) {
		try {
			Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
			Long planId = Long.valueOf(request.get("planId").toString());
			securityService.requireAdminOrOwner(requesterId, usuarioId);
			Suscripcion suscripcion = suscripcionService.crearSuscripcion(usuarioId, planId);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toSuscripcionDTO(suscripcion));
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	// Cambia el plan de una suscripción (genera prorrateo si es upgrade).
	@PostMapping("/cambiar-plan")
	public ResponseEntity<?> cambiarPlan(@RequestBody CambiarPlanRequest request,
			@RequestParam(required = false) Long requesterId) {
		try {
			Suscripcion suscripcion = suscripcionService.buscarPorId(request.getSuscripcionId())
					.orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
			securityService.requireAdminOrOwner(requesterId, suscripcion.getUsuario().getId());
			Suscripcion actualizada = suscripcionService.cambiarPlan(request.getSuscripcionId(), request.getNuevoPlanId());
			return ResponseEntity.ok(mapper.toSuscripcionDTO(actualizada));
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	// Obtiene la suscripción activa de un usuario.
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<?> obtenerSuscripcionActiva(@PathVariable Long usuarioId,
			@RequestParam(required = false) Long requesterId) {
		try {
			securityService.requireAdminOrOwner(requesterId, usuarioId);
			return suscripcionService.obtenerSuscripcionActiva(usuarioId)
					.map(mapper::toSuscripcionDTO)
					.map(ResponseEntity::ok)
					.orElse(ResponseEntity.notFound().build());
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		}
	}

	// Cancela una suscripción.
	@PostMapping("/{id}/cancelar")
	public ResponseEntity<?> cancelarSuscripcion(@PathVariable Long id,
			@RequestBody(required = false) Map<String, String> request,
			@RequestParam(required = false) Long requesterId) {
		try {
			Suscripcion suscripcion = suscripcionService.buscarPorId(id)
					.orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
			securityService.requireAdminOrOwner(requesterId, suscripcion.getUsuario().getId());
			String motivo = request != null ? request.get("motivo") : "Cancelación por el usuario";
			Suscripcion cancelada = suscripcionService.cancelarSuscripcion(id, motivo);
			return ResponseEntity.ok(mapper.toSuscripcionDTO(cancelada));
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	// Obtiene las facturas de un usuario.
	@GetMapping("/usuario/{usuarioId}/facturas")
	public ResponseEntity<?> obtenerFacturas(@PathVariable Long usuarioId,
			@RequestParam(required = false) Long requesterId) {
		try {
			securityService.requireAdminOrOwner(requesterId, usuarioId);
			List<FacturaDTO> facturas = suscripcionService.obtenerFacturasUsuario(usuarioId).stream()
					.map(mapper::toFacturaDTO).toList();
			return ResponseEntity.ok(facturas);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
		}
	}

	// Paga una factura.
	@PostMapping("/facturas/{facturaId}/pagar")
	public ResponseEntity<?> pagarFactura(@PathVariable Long facturaId) {
		try {
			Factura factura = suscripcionService.pagarFactura(facturaId);
			return ResponseEntity.ok(mapper.toFacturaDTO(factura));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
}
