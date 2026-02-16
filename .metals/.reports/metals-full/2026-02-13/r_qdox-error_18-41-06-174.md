error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/SuscripcionController.java
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/SuscripcionController.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[126,12]

error in qdox parser
file content:
```java
offset: 5159
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

        var maybeRequester = usuarioService.buscarPorId(requesterId);
        if (maybeRequester.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        var requester = maybeRequester.get();
        boolean allowed = requester.getRole() == com.Martin.SaaS.model.enums.Role.ADMIN || requester.getId().equals(usuarioId);
        if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return suscripcionService.obtenerSuscripcionActiva(usuarioId)
            .map(mapper::toSuscripcionDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
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

        var maybeRequester = usuarioService.buscarPorId(requesterId);
        if (maybeRequester.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        var requester = maybeRequester.get();
        boolean allowed = requester.getRole() == com.Martin.SaaS.model.enums.Role.ADMIN || requester.getId().equals(usuarioId);
        if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        List<FacturaDTO> facturas = suscripcionService.obtenerFacturasUsuario(usuarioId).stream()
            .map(mapper::toFacturaDTO)
            .toList();
        return ResponseEntity.ok(facturas);
        }
    }

    /**
     * Paga una factura.
     */
    @PostMapping("/facturas/{facturaId}/pagar")
    public R@@esponseEntity<?> pagarFactura(@PathVariable Long facturaId) {
        try {
            Factura factura = suscripcionService.pagarFactura(facturaId);
            return ResponseEntity.ok(mapper.toFacturaDTO(factura));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:560)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:691)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:688)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:688)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:936)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1583)
```
#### Short summary: 

QDox parse error in file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/controller/SuscripcionController.java