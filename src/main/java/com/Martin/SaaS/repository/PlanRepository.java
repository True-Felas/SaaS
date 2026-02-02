package com.Martin.SaaS.repository;

import com.Martin.SaaS.model.Plan;
import com.Martin.SaaS.model.enums.TipoPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Plan.
 */
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    /**
     * Busca un plan por su tipo.
     */
    Optional<Plan> findByTipo(TipoPlan tipo);

    /**
     * Busca todos los planes activos.
     */
    List<Plan> findByActivoTrue();

    /**
     * Busca todos los planes activos ordenados por precio.
     */
    List<Plan> findByActivoTrueOrderByPrecioMensualAsc();

    /**
     * Verifica si existe un plan con el tipo dado.
     */
    boolean existsByTipo(TipoPlan tipo);
}
