package com.Martin.SaaS.repository;

import com.Martin.SaaS.model.Factura;
import com.Martin.SaaS.model.enums.EstadoFactura;
import com.Martin.SaaS.model.enums.TipoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Factura.
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Busca una factura por número.
     */
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    /**
     * Busca facturas por suscripción.
     */
    List<Factura> findBySuscripcionId(Long suscripcionId);

    /**
     * Busca facturas por usuario.
     */
    @Query("SELECT f FROM Factura f WHERE f.suscripcion.usuario.id = :usuarioId ORDER BY f.fechaEmision DESC")
    List<Factura> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca facturas por estado.
     */
    List<Factura> findByEstado(EstadoFactura estado);

    /**
     * Busca facturas pendientes de un usuario.
     */
    @Query("SELECT f FROM Factura f WHERE f.suscripcion.usuario.id = :usuarioId AND f.estado = 'PENDIENTE'")
    List<Factura> findFacturasPendientesByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca facturas vencidas.
     */
    @Query("SELECT f FROM Factura f WHERE f.estado = 'PENDIENTE' AND f.fechaVencimiento < :fecha")
    List<Factura> findFacturasVencidas(@Param("fecha") LocalDate fecha);

    /**
     * Busca facturas vencidas a la fecha actual.
     */
    default List<Factura> findFacturasVencidasHoy() {
        return findFacturasVencidas(LocalDate.now());
    }

    /**
     * Busca facturas por tipo.
     */
    List<Factura> findByTipo(TipoFactura tipo);

    /**
     * Busca facturas de prorrateo de un usuario.
     */
    @Query("SELECT f FROM Factura f WHERE f.suscripcion.usuario.id = :usuarioId AND f.tipo = 'PRORRATEO'")
    List<Factura> findFacturasProrrateoByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca facturas en un rango de fechas.
     */
    List<Factura> findByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
