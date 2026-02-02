package com.Martin.SaaS.repository;

import com.Martin.SaaS.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Perfil.
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    /**
     * Busca un perfil por usuario ID.
     */
    Optional<Perfil> findByUsuarioId(Long usuarioId);

    /**
     * Verifica si existe un perfil para el usuario.
     */
    boolean existsByUsuarioId(Long usuarioId);
}
