package com.Martin.SaaS.repository;

import com.Martin.SaaS.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por email.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios activos.
     */
    List<Usuario> findByActivoTrue();

    /**
     * Busca usuarios con suscripción activa.
     */
    @Query("SELECT u FROM Usuario u JOIN u.suscripciones s WHERE s.estado = 'ACTIVA'")
    List<Usuario> findUsuariosConSuscripcionActiva();

    /**
     * Busca usuarios por nombre o apellido.
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> buscarPorNombreOApellido(@Param("texto") String texto);
}
