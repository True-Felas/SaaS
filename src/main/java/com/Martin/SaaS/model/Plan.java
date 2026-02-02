package com.Martin.SaaS.model;

import com.Martin.SaaS.model.enums.TipoPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Plan - Define los diferentes planes de suscripción disponibles.
 * Basic, Premium, Enterprise con sus características y precios.
 */
@Entity
@Table(name = "planes")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TipoPlan tipo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "precio_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMensual;

    @DecimalMin(value = "0.00")
    @Column(name = "precio_anual", precision = 10, scale = 2)
    private BigDecimal precioAnual;

    /** Número máximo de usuarios permitidos (null = ilimitado) */
    @Column(name = "max_usuarios")
    private Integer maxUsuarios;

    /** Almacenamiento en GB (null = ilimitado) */
    @Column(name = "almacenamiento_gb")
    private Integer almacenamientoGb;

    /** Número máximo de proyectos (null = ilimitado) */
    @Column(name = "max_proyectos")
    private Integer maxProyectos;

    @Column(name = "soporte_prioritario")
    @Builder.Default
    private Boolean soportePrioritario = false;

    @Column(name = "api_acceso")
    @Builder.Default
    private Boolean apiAcceso = false;

    @Column(name = "analytics_avanzados")
    @Builder.Default
    private Boolean analyticsAvanzados = false;

    @Column(name = "integraciones_externas")
    @Builder.Default
    private Boolean integracionesExternas = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    // Relación One-to-Many con Suscripciones
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Suscripcion> suscripciones = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    /**
     * Calcula el precio diario del plan basado en el precio mensual.
     * Asume 30 días por mes para el cálculo.
     * @return Precio por día
     */
    public BigDecimal getPrecioDiario() {
        return precioMensual.divide(BigDecimal.valueOf(30), 4, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Verifica si este plan es un upgrade respecto a otro plan.
     * @param otroPlan El plan a comparar
     * @return true si este plan es de mayor nivel
     */
    public boolean esUpgradeDe(Plan otroPlan) {
        return this.tipo.esUpgradeDe(otroPlan.getTipo());
    }

    /**
     * Calcula la diferencia de precio mensual con otro plan.
     * @param otroPlan El plan a comparar
     * @return Diferencia de precio (positivo si este es más caro)
     */
    public BigDecimal diferenciaPrecio(Plan otroPlan) {
        return this.precioMensual.subtract(otroPlan.getPrecioMensual());
    }
}
