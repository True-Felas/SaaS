package com.Martin.SaaS.repository;

import com.Martin.SaaS.model.Suscripcion;
import com.Martin.SaaS.model.Usuario;
import com.Martin.SaaS.model.enums.EstadoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Repositorio para la entidad Suscripcion.
@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

	// Busca suscripciones por usuario.
	List<Suscripcion> findByUsuario(Usuario usuario);

	// Busca suscripciones por usuario ID.
	List<Suscripcion> findByUsuarioId(Long usuarioId);

	// Busca la suscripción activa de un usuario.
	@Query("SELECT s FROM Suscripcion s WHERE s.usuario.id = :usuarioId AND s.estado IN ('ACTIVA', 'TRIAL')")
	Optional<Suscripcion> findSuscripcionActivaByUsuarioId(@Param("usuarioId") Long usuarioId);

	// Busca suscripciones por estado.
	List<Suscripcion> findByEstado(EstadoSuscripcion estado);

	// Busca suscripciones que deben ser cobradas (fecha próximo cobro <= hoy).
	@Query("SELECT s FROM Suscripcion s WHERE s.estado = 'ACTIVA' AND s.fechaProximoCobro <= :fecha")
	List<Suscripcion> findSuscripcionesParaCobrar(@Param("fecha") LocalDate fecha);

	// Busca suscripciones que deben ser cobradas hoy.
	default List<Suscripcion> findSuscripcionesParaCobrarHoy() {
		return findSuscripcionesParaCobrar(LocalDate.now());
	}

	// Busca suscripciones morosas.
	List<Suscripcion> findByEstadoAndFechaProximoCobroBefore(EstadoSuscripcion estado, LocalDate fecha);

	// Cuenta suscripciones activas por plan.
	@Query("SELECT COUNT(s) FROM Suscripcion s WHERE s.plan.id = :planId AND s.estado = 'ACTIVA'")
	Long countSuscripcionesActivasByPlanId(@Param("planId") Long planId);
}
