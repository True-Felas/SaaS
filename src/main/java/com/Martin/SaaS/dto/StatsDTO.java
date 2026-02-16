package com.Martin.SaaS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
	private Long totalUsuarios;
	private Long suscripcionesActivas;
	private BigDecimal ingresosTotales;
	private Map<String, Long> distribucionPlanes;
	private List<IngresoMensualDTO> ingresosMensuales;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IngresoMensualDTO {
		private String mes;
		private BigDecimal monto;
	}
}
