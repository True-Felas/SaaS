package com.Martin.SaaS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO para transferir información de facturas.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
	private Long id;
	private String numeroFactura;
	private String tipo;
	private String estado;
	private BigDecimal subtotal;
	private BigDecimal descuento;
	private BigDecimal impuesto;
	private BigDecimal total;
	private LocalDate periodoInicio;
	private LocalDate periodoFin;
	private LocalDate fechaEmision;
	private LocalDate fechaVencimiento;
	private LocalDate fechaPago;
	private String concepto;
	private Integer diasProrrateados;
	private String planAnterior;
	private String planNuevo;
}
