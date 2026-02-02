package com.Martin.SaaS.config;

import com.Martin.SaaS.model.Plan;
import com.Martin.SaaS.model.enums.TipoPlan;
import com.Martin.SaaS.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Inicializador de datos para la aplicación.
 * Carga los planes por defecto al arrancar.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final PlanRepository planRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Inicializando datos de la aplicación SaaS...");
        
        inicializarPlanes();
        
        log.info("Datos inicializados correctamente.");
    }

    private void inicializarPlanes() {
        if (planRepository.count() > 0) {
            log.info("Los planes ya existen en la base de datos.");
            return;
        }

        // Plan BASIC
        Plan planBasic = Plan.builder()
                .tipo(TipoPlan.BASIC)
                .nombre("Plan Básico")
                .descripcion("Ideal para empezar. Incluye las funcionalidades esenciales.")
                .precioMensual(new BigDecimal("9.99"))
                .precioAnual(new BigDecimal("99.99"))
                .maxUsuarios(1)
                .almacenamientoGb(5)
                .maxProyectos(3)
                .soportePrioritario(false)
                .apiAcceso(false)
                .analyticsAvanzados(false)
                .integracionesExternas(false)
                .activo(true)
                .build();

        // Plan PREMIUM
        Plan planPremium = Plan.builder()
                .tipo(TipoPlan.PREMIUM)
                .nombre("Plan Premium")
                .descripcion("Para profesionales. Más usuarios, almacenamiento y soporte prioritario.")
                .precioMensual(new BigDecimal("29.99"))
                .precioAnual(new BigDecimal("299.99"))
                .maxUsuarios(5)
                .almacenamientoGb(50)
                .maxProyectos(20)
                .soportePrioritario(true)
                .apiAcceso(true)
                .analyticsAvanzados(false)
                .integracionesExternas(true)
                .activo(true)
                .build();

        // Plan ENTERPRISE
        Plan planEnterprise = Plan.builder()
                .tipo(TipoPlan.ENTERPRISE)
                .nombre("Plan Enterprise")
                .descripcion("Solución completa para empresas. Sin límites y con todas las funcionalidades.")
                .precioMensual(new BigDecimal("99.99"))
                .precioAnual(new BigDecimal("999.99"))
                .maxUsuarios(null) // Ilimitado
                .almacenamientoGb(null) // Ilimitado
                .maxProyectos(null) // Ilimitado
                .soportePrioritario(true)
                .apiAcceso(true)
                .analyticsAvanzados(true)
                .integracionesExternas(true)
                .activo(true)
                .build();

        planRepository.save(planBasic);
        planRepository.save(planPremium);
        planRepository.save(planEnterprise);

        log.info("Planes creados: BASIC ({}€), PREMIUM ({}€), ENTERPRISE ({}€)",
                planBasic.getPrecioMensual(),
                planPremium.getPrecioMensual(),
                planEnterprise.getPrecioMensual());
    }
}
