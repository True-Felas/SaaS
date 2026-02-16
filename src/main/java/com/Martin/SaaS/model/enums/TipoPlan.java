package com.Martin.SaaS.model.enums;

// Tipos de planes disponibles en el sistema SaaS. Cada plan tiene un nivel jerárquico para facilitar comparaciones de upgrade/downgrade.
public enum TipoPlan {

	BASIC(1, "Básico"), PREMIUM(2, "Premium"), ENTERPRISE(3, "Enterprise");

	private final int nivel;
	private final String descripcion;

	TipoPlan(int nivel, String descripcion) {
		this.nivel = nivel;
		this.descripcion = descripcion;
	}

	public int getNivel() {
		return nivel;
	}

	public String getDescripcion() {
		return descripcion;
	}

	// Verifica si este plan es un upgrade respecto a otro. @param otro El plan a
	// comparar. @return true si este plan es de mayor nivel
	public boolean esUpgradeDe(TipoPlan otro) {
		return this.nivel > otro.nivel;
	}

	// Verifica si este plan es un downgrade respecto a otro. @param otro El plan a
	// comparar. @return true si este plan es de menor nivel
	public boolean esDowngradeDe(TipoPlan otro) {
		return this.nivel < otro.nivel;
	}
}
