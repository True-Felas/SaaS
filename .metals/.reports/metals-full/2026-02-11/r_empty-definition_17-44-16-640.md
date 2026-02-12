error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/service/SuscripcionService.java:com/Martin/SaaS/service/SuscripcionService#obtenerPorcentajeImpuestoPorPais#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/service/SuscripcionService.java
empty definition using pc, found symbol in pc: com/Martin/SaaS/service/SuscripcionService#obtenerPorcentajeImpuestoPorPais#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 3859
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/service/SuscripcionService.java
text:
```scala
package com.Martin.SaaS.service;

import com.Martin.SaaS.model.*;
import com.Martin.SaaS.model.enums.*;
import com.Martin.SaaS.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de suscripciones y facturación.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final FacturaRepository facturaRepository;
    private final PlanRepository planRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea una nueva suscripción para un usuario.
     */
    public Suscripcion crearSuscripcion(Long usuarioId, Long planId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        // Verificar si ya tiene una suscripción activa
        Optional<Suscripcion> suscripcionActiva = suscripcionRepository.findSuscripcionActivaByUsuarioId(usuarioId);
        if (suscripcionActiva.isPresent()) {
            throw new RuntimeException("El usuario ya tiene una suscripción activa");
        }

        Suscripcion suscripcion = Suscripcion.builder()
                .usuario(usuario)
                .plan(plan)
                .estado(EstadoSuscripcion.ACTIVA)
                .fechaInicio(LocalDate.now())
                .fechaProximoCobro(LocalDate.now().plusDays(30))
                .autoRenovacion(true)
                .periodoFacturacionDias(30)
                .build();

        suscripcion = suscripcionRepository.save(suscripcion);

        // Generar primera factura
        generarFacturaMensual(suscripcion);

        log.info("Suscripción creada: Usuario {} - Plan {}", usuario.getEmail(), plan.getNombre());
        return suscripcion;
    }

    /**
     * Cambia el plan de una suscripción existente.
     * Si es un upgrade, genera factura de prorrateo.
     */
    public Suscripcion cambiarPlan(Long suscripcionId, Long nuevoPlanId) {
        Suscripcion suscripcion = suscripcionRepository.findById(suscripcionId)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

        Plan planAnterior = suscripcion.getPlan();
        Plan planNuevo = planRepository.findById(nuevoPlanId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        if (planAnterior.getId().equals(planNuevo.getId())) {
            throw new RuntimeException("Ya estás suscrito a este plan");
        }

        // Si es un upgrade (plan más caro), generar prorrateo
        if (planNuevo.esUpgradeDe(planAnterior)) {
            generarFacturaProrrateo(suscripcion, planAnterior, planNuevo);
        }

        suscripcion.setPlan(planNuevo);
        suscripcion = suscripcionRepository.save(suscripcion);

        log.info("Plan cambiado: {} -> {}", planAnterior.getNombre(), planNuevo.getNombre());
        return suscripcion;
    }

    /**
     * Genera una factura mensual para una suscripción.
     */
    public Factura generarFacturaMensual(Suscripcion suscripcion) {
        Plan plan = suscripcion.getPlan();
        Usuario usuario = suscripcion.getUsuario();
        String pais = usuario.getPerfil() != null ? usuario.getPerfil().getPais() : "ES";

        BigDecimal porcentajeImpuesto = @@obtenerPorcentajeImpuestoPorPais(pais);

        Factura factura = Factura.builder()
            .suscripcion(suscripcion)
            .tipo(TipoFactura.MENSUAL)
            .estado(EstadoFactura.PENDIENTE)
            .subtotal(plan.getPrecioMensual())
            .porcentajeImpuesto(porcentajeImpuesto)
            .concepto("Suscripción " + plan.getNombre() + " - Mensual")
            .periodoInicio(LocalDate.now())
            .periodoFin(LocalDate.now().plusDays(30))
            .fechaEmision(LocalDate.now())
            .fechaVencimiento(LocalDate.now().plusDays(30))
            .build();

        factura = facturaRepository.save(factura);
        suscripcion.addFactura(factura);

        log.info("Factura mensual generada: {} - {}€", factura.getNumeroFactura(), factura.getTotal());
        return factura;
        
        BigDecimal obtenerPorcentajeImpuestoPorPais(String pais) {
            switch (pais != null ? pais.toUpperCase() : "ES") {
                case "ES": // España
                    return new BigDecimal("21.00");
                case "MX": // México
                    return new BigDecimal("16.00");
                case "AR": // Argentina
                    return new BigDecimal("21.00");
                case "US": // USA
                    return new BigDecimal("8.00");
                case "CO": // Colombia
                    return new BigDecimal("19.00");
                default:
                    return new BigDecimal("21.00"); // Default (España)
            }
        }
    }

    /**
     * Genera una factura de prorrateo al cambiar a un plan más caro.
     */
    public Factura generarFacturaProrrateo(Suscripcion suscripcion, Plan planAnterior, Plan planNuevo) {
        // Calcular días restantes hasta el próximo cobro
        long diasRestantes = suscripcion.getDiasHastaProximoCobro();
        if (diasRestantes <= 0) {
            diasRestantes = 30;
        }

        // Calcular diferencia de precio diario
        BigDecimal precioDiarioAnterior = planAnterior.getPrecioDiario();
        BigDecimal precioDiarioNuevo = planNuevo.getPrecioDiario();
        BigDecimal diferenciaDiaria = precioDiarioNuevo.subtract(precioDiarioAnterior);

        // Calcular prorrateo
        BigDecimal subtotalProrrateo = diferenciaDiaria
                .multiply(BigDecimal.valueOf(diasRestantes))
                .setScale(2, RoundingMode.HALF_UP);

        Factura factura = Factura.builder()
                .suscripcion(suscripcion)
                .tipo(TipoFactura.PRORRATEO)
                .estado(EstadoFactura.PENDIENTE)
                .subtotal(subtotalProrrateo)
                .concepto("Prorrateo cambio de plan: " + planAnterior.getNombre() + " → " + planNuevo.getNombre())
                .diasProrrateados((int) diasRestantes)
                .planAnterior(planAnterior.getNombre())
                .planNuevo(planNuevo.getNombre())
                .periodoInicio(LocalDate.now())
                .periodoFin(suscripcion.getFechaProximoCobro())
                .fechaEmision(LocalDate.now())
                .fechaVencimiento(LocalDate.now().plusDays(7))
                .build();

        factura = facturaRepository.save(factura);
        suscripcion.addFactura(factura);

        log.info("Factura prorrateo generada: {} - {}€ ({} días)", 
                factura.getNumeroFactura(), factura.getTotal(), diasRestantes);
        return factura;
    }

    /**
     * Procesa las suscripciones que deben ser cobradas hoy.
     */
    public void procesarCobrosAutomaticos() {
        List<Suscripcion> suscripciones = suscripcionRepository.findSuscripcionesParaCobrarHoy();
        
        for (Suscripcion suscripcion : suscripciones) {
            if (suscripcion.getAutoRenovacion()) {
                generarFacturaMensual(suscripcion);
                suscripcion.setFechaUltimoCobro(LocalDate.now());
                suscripcion.setFechaProximoCobro(LocalDate.now().plusDays(suscripcion.getPeriodoFacturacionDias()));
                suscripcionRepository.save(suscripcion);
            }
        }
        
        log.info("Cobros automáticos procesados: {} suscripciones", suscripciones.size());
    }

    /**
     * Cancela una suscripción.
     */
    public Suscripcion cancelarSuscripcion(Long suscripcionId, String motivo) {
        Suscripcion suscripcion = suscripcionRepository.findById(suscripcionId)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

        suscripcion.cancelar(motivo);
        return suscripcionRepository.save(suscripcion);
    }

    /**
     * Obtiene la suscripción activa de un usuario.
     */
    @Transactional(readOnly = true)
    public Optional<Suscripcion> obtenerSuscripcionActiva(Long usuarioId) {
        return suscripcionRepository.findSuscripcionActivaByUsuarioId(usuarioId);
    }

    /**
     * Obtiene las facturas de un usuario.
     */
    @Transactional(readOnly = true)
    public List<Factura> obtenerFacturasUsuario(Long usuarioId) {
        return facturaRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Paga una factura.
     */
    public Factura pagarFactura(Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        factura.marcarComoPagada();
        return facturaRepository.save(factura);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/Martin/SaaS/service/SuscripcionService#obtenerPorcentajeImpuestoPorPais#