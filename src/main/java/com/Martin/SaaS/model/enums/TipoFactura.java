package com.Martin.SaaS.model.enums;

// Tipos de facturas generadas en el sistema.
public enum TipoFactura {

	// Factura mensual regular
	MENSUAL("Factura Mensual"),

	// Factura por prorrateo al cambiar de plan
	PRORRATEO("Factura por Prorrateo"),

	// Factura de ajuste o corrección
	AJUSTE("Factura de Ajuste"),

	// Factura de reembolso
	REEMBOLSO("Nota de Crédito");

	private final String descripcion;

	TipoFactura(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
