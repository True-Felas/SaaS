package com.Martin.SaaS.model.enums;

// Tipos de métodos de pago soportados.
public enum TipoMetodoPago {

	TARJETA_CREDITO("Tarjeta de Crédito"), TARJETA_DEBITO("Tarjeta de Débito"), PAYPAL("PayPal"),
	TRANSFERENCIA_BANCARIA("Transferencia Bancaria");

	private final String descripcion;

	TipoMetodoPago(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
