package com.Martin.SaaS.repository;

import com.Martin.SaaS.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio para la entidad MetodoPago.
@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {

	// Busca métodos de pago por usuario.
	List<MetodoPago> findByUsuarioId(Long usuarioId);

	// Busca métodos de pago activos por usuario.
	List<MetodoPago> findByUsuarioIdAndActivoTrue(Long usuarioId);

	// Busca el método de pago principal de un usuario.
	@Query("SELECT m FROM MetodoPago m WHERE m.usuario.id = :usuarioId AND m.esPrincipal = true AND m.activo = true")
	Optional<MetodoPago> findMetodoPagoPrincipalByUsuarioId(@Param("usuarioId") Long usuarioId);

	// Cuenta métodos de pago activos de un usuario.
	Long countByUsuarioIdAndActivoTrue(Long usuarioId);
}
