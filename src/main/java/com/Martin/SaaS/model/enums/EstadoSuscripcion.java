package com.Martin.SaaS.model.enums;

/**
 * Estados posibles de una suscripción en el sistema SaaS.
 */
public enum EstadoSuscripcion {
    
    /** Suscripción activa y al día con los pagos */
    ACTIVA("Activa"),
    
    /** Suscripción cancelada por el usuario o el sistema */
    CANCELADA("Cancelada"),
    
    /** Suscripción con pagos pendientes */
    MOROSA("Morosa"),
    
    /** Suscripción pausada temporalmente */
    PAUSADA("Pausada"),
    
    /** Suscripción en periodo de prueba */
    TRIAL("Periodo de Prueba");

    private final String descripcion;

    EstadoSuscripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
