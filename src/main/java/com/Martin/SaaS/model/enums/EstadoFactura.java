package com.Martin.SaaS.model.enums;

/**
 * Estados posibles de una factura.
 */
public enum EstadoFactura {
    
    /** Factura generada pero pendiente de pago */
    PENDIENTE("Pendiente de Pago"),
    
    /** Factura pagada correctamente */
    PAGADA("Pagada"),
    
    /** Factura vencida sin pagar */
    VENCIDA("Vencida"),
    
    /** Factura cancelada */
    CANCELADA("Cancelada"),
    
    /** Factura reembolsada */
    REEMBOLSADA("Reembolsada");

    private final String descripcion;

    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
