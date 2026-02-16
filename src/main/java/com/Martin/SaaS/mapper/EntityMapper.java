package com.Martin.SaaS.mapper;

import com.Martin.SaaS.dto.*;
import com.Martin.SaaS.model.*;
import org.springframework.stereotype.Component;

// Mapper para convertir entre entidades y DTOs.
@Component
public class EntityMapper {

	public PlanDTO toPlanDTO(Plan plan) {
		if (plan == null)
			return null;

		return PlanDTO.builder().id(plan.getId()).tipo(plan.getTipo().name()).nombre(plan.getNombre())
				.descripcion(plan.getDescripcion()).precioMensual(plan.getPrecioMensual())
				.precioAnual(plan.getPrecioAnual()).maxUsuarios(plan.getMaxUsuarios())
				.almacenamientoGb(plan.getAlmacenamientoGb()).maxProyectos(plan.getMaxProyectos())
				.soportePrioritario(plan.getSoportePrioritario()).apiAcceso(plan.getApiAcceso())
				.analyticsAvanzados(plan.getAnalyticsAvanzados()).integracionesExternas(plan.getIntegracionesExternas())
				.build();
	}

	public UsuarioDTO toUsuarioDTO(Usuario usuario) {
		if (usuario == null)
			return null;

		Suscripcion suscripcionActiva = usuario.getSuscripcionActiva();

		return UsuarioDTO.builder().id(usuario.getId()).email(usuario.getEmail()).nombre(usuario.getNombre())
				.apellido(usuario.getApellido()).activo(usuario.getActivo())
				.emailVerificado(usuario.getEmailVerificado()).fechaRegistro(usuario.getFechaRegistro())
				.suscripcionActiva(toSuscripcionDTO(suscripcionActiva))
				.role(usuario.getRole() != null ? usuario.getRole().name() : null).build();
	}

	public PublicUsuarioDTO toPublicUsuarioDTO(Usuario usuario) {
		if (usuario == null)
			return null;

		Suscripcion sub = usuario.getSuscripcionActiva();

		return PublicUsuarioDTO.builder().id(usuario.getId()).nombre(usuario.getNombre())
				.apellido(usuario.getApellido()).planNombre(sub != null ? sub.getPlan().getNombre() : "Sin plan")
				.fechaRegistro(usuario.getFechaRegistro()).build();
	}

	public SuscripcionDTO toSuscripcionDTO(Suscripcion suscripcion) {
		if (suscripcion == null)
			return null;

		return SuscripcionDTO.builder().id(suscripcion.getId()).usuarioId(suscripcion.getUsuario().getId())
				.usuarioEmail(suscripcion.getUsuario().getEmail()).planId(suscripcion.getPlan().getId())
				.planNombre(suscripcion.getPlan().getNombre()).planTipo(suscripcion.getPlan().getTipo().name())
				.estado(suscripcion.getEstado().name()).fechaInicio(suscripcion.getFechaInicio())
				.fechaFin(suscripcion.getFechaFin()).fechaProximoCobro(suscripcion.getFechaProximoCobro())
				.autoRenovacion(suscripcion.getAutoRenovacion()).build();
	}

	public FacturaDTO toFacturaDTO(Factura factura) {
		if (factura == null)
			return null;

		return FacturaDTO.builder().id(factura.getId()).numeroFactura(factura.getNumeroFactura())
				.tipo(factura.getTipo().name()).estado(factura.getEstado().name()).subtotal(factura.getSubtotal())
				.descuento(factura.getDescuento()).impuesto(factura.getImpuesto()).total(factura.getTotal())
				.periodoInicio(factura.getPeriodoInicio()).periodoFin(factura.getPeriodoFin())
				.fechaEmision(factura.getFechaEmision()).fechaVencimiento(factura.getFechaVencimiento())
				.fechaPago(factura.getFechaPago()).concepto(factura.getConcepto())
				.diasProrrateados(factura.getDiasProrrateados()).planAnterior(factura.getPlanAnterior())
				.planNuevo(factura.getPlanNuevo()).build();
	}
}
