package com.Martin.SaaS.service;

import com.Martin.SaaS.model.Usuario;
import com.Martin.SaaS.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Servicio centralizado de verificación de autorización.
 * Mueve toda la lógica de seguridad fuera de los controllers.
 */
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioService usuarioService;

    /**
     * Resuelve el usuario solicitante por ID.
     * Lanza 403 si el ID es null y 401 si no existe.
     */
    public Usuario resolverRequester(Long requesterId) {
        if (requesterId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "requesterId requerido");
        }
        return usuarioService.buscarPorId(requesterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario solicitante no encontrado"));
    }

    /**
     * Retorna true si el usuario tiene rol ADMIN.
     */
    public boolean esAdmin(Long requesterId) {
        if (requesterId == null) return false;
        return usuarioService.buscarPorId(requesterId)
                .map(u -> u.getRole() == Role.ADMIN)
                .orElse(false);
    }

    /**
     * Lanza 403 si el solicitante no es ADMIN.
     */
    public void requireAdmin(Long requesterId) {
        Usuario requester = resolverRequester(requesterId);
        if (requester.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado. Se requiere rol ADMIN.");
        }
    }

    /**
     * Lanza 403 si el solicitante no es owner del recurso ni ADMIN.
     */
    public void requireAdminOrOwner(Long requesterId, Long ownerId) {
        Usuario requester = resolverRequester(requesterId);
        boolean allowed = requester.getRole() == Role.ADMIN || requester.getId().equals(ownerId);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta operación.");
        }
    }

    /**
     * Retorna el usuario si es ADMIN o si coincide con targetId,
     * sino lanza 403.
     */
    public Usuario resolverRequesterConOwnership(Long requesterId, Long ownerId) {
        Usuario requester = resolverRequester(requesterId);
        boolean allowed = requester.getRole() == Role.ADMIN || requester.getId().equals(ownerId);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta operación.");
        }
        return requester;
    }

    /**
     * Resuelve sin verificar ownership (solo que el requester exista y esté autenticado).
     * Retorna el Optional del usuario.
     */
    public Optional<Usuario> resolverRequesterOptional(Long requesterId) {
        if (requesterId == null) return Optional.empty();
        return usuarioService.buscarPorId(requesterId);
    }
}
