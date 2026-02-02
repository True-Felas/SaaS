package com.Martin.SaaS.config;

import org.hibernate.envers.RevisionListener;

/**
 * Listener personalizado para las revisiones de Envers.
 * Captura información adicional durante las auditorías.
 */
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;
        
        // En un entorno con Spring Security, aquí obtendrías el usuario actual
        // Por ahora usamos valores por defecto
        revision.setUsuarioModificacion("system");
        revision.setIpAddress("127.0.0.1");
        
        // Si tuvieras Spring Security:
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // if (auth != null && auth.isAuthenticated()) {
        //     revision.setUsuarioModificacion(auth.getName());
        // }
    }
}
