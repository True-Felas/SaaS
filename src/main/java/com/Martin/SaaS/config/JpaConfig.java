package com.Martin.SaaS.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración de JPA con auditoría habilitada.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // La auditoría de Envers se configura automáticamente con @Audited en las entidades
    // Este @EnableJpaAuditing es para usar anotaciones como @CreatedDate, @LastModifiedDate
}
