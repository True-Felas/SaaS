package com.Martin.SaaS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DTO para transferir información de suscripciones.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionDTO {
	private Long id;
	private Long usuarioId;
	private String usuarioEmail;
	private Long planId;
	private String planNombre;
	private String planTipo;
	private String estado;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private LocalDate fechaProximoCobro;
	private Boolean autoRenovacion;
}
