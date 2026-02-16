error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/SuscripcionController.java:com/Martin/SaaS/service/UsuarioService#buscarPorId#map#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/SuscripcionController.java
empty definition using pc, found symbol in pc: com/Martin/SaaS/service/UsuarioService#buscarPorId#map#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 2801
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/SuscripcionController.java
text:
```scala
package com.Martin.SaaS.controller;

import com.Martin.SaaS.dto.CambiarPlanRequest;
import com.Martin.SaaS.dto.FacturaDTO;
import com.Martin.SaaS.dto.SuscripcionDTO;
import com.Martin.SaaS.mapper.EntityMapper;
import com.Martin.SaaS.model.Factura;
import com.Martin.SaaS.model.Suscripcion;
import com.Martin.SaaS.service.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de suscripciones.
 */
@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;
    private final EntityMapper mapper;
    private final com.Martin.SaaS.service.UsuarioService usuarioService;

    /**
     * Crea una nueva suscripción para un usuario.
     */
    @PostMapping
    public ResponseEntity<?> crearSuscripcion(@RequestBody Map<String, Long> request) {
        try {
            Long usuarioId = request.get("usuarioId");
            Long planId = request.get("planId");
            
            Suscripcion suscripcion = suscripcionService.crearSuscripcion(usuarioId, planId);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toSuscripcionDTO(suscripcion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Cambia el plan de una suscripción (genera prorrateo si es upgrade).
     */
    @PostMapping("/cambiar-plan")
    public ResponseEntity<?> cambiarPlan(@RequestBody CambiarPlanRequest request) {
        try {
            Suscripcion suscripcion = suscripcionService.cambiarPlan(
                    request.getSuscripcionId(), 
                    request.getNuevoPlanId()
            );
            return ResponseEntity.ok(mapper.toSuscripcionDTO(suscripcion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene la suscripción activa de un usuario.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<SuscripcionDTO> obtenerSuscripcionActiva(@PathVariable Long usuarioId,
                                                                   @RequestParam(required = false) Long requesterId) {
        if (requesterId == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return (ResponseEntity<SuscripcionDTO>) usuarioService.buscarPorId(requesterId)
                .@@map(requester -> {
                    boolean allowed = requester.getRole() == com.Martin.SaaS.model.enums.Role.ADMIN || requester.getId().equals(usuarioId);
                    if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    return suscripcionService.obtenerSuscripcionActiva(usuarioId)
                            .map(mapper::toSuscripcionDTO)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    /**
     * Cancela una suscripción.
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarSuscripcion(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> request) {
        try {
            String motivo = request != null ? request.get("motivo") : "Cancelación por el usuario";
            Suscripcion suscripcion = suscripcionService.cancelarSuscripcion(id, motivo);
            return ResponseEntity.ok(mapper.toSuscripcionDTO(suscripcion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene las facturas de un usuario.
     */
    @GetMapping("/usuario/{usuarioId}/facturas")
    public ResponseEntity<List<FacturaDTO>> obtenerFacturas(@PathVariable Long usuarioId,
                                                            @RequestParam(required = false) Long requesterId) {
        if (requesterId == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return usuarioService.buscarPorId(requesterId)
                .map(requester -> {
                    boolean allowed = requester.getRole() == com.Martin.SaaS.model.enums.Role.ADMIN || requester.getId().equals(usuarioId);
                    if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    List<FacturaDTO> facturas = suscripcionService.obtenerFacturasUsuario(usuarioId).stream()
                            .map(mapper::toFacturaDTO)
                            .toList();
                    return ResponseEntity.ok(facturas);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    /**
     * Paga una factura.
     */
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

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/Martin/SaaS/service/UsuarioService#buscarPorId#map#