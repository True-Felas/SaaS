package com.Martin.SaaS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para transferir información de planes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    private Long id;
    private String tipo;
    private String nombre;
    private String descripcion;
    private BigDecimal precioMensual;
    private BigDecimal precioAnual;
    private Integer maxUsuarios;
    private Integer almacenamientoGb;
    private Integer maxProyectos;
    private Boolean soportePrioritario;
    private Boolean apiAcceso;
    private Boolean analyticsAvanzados;
    private Boolean integracionesExternas;
}
