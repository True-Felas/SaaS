package com.Martin.SaaS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de JPA con auditoría habilitada.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // La auditoría de Envers se configura automáticamente con @Audited en las entidades
    // Este @EnableJpaAuditing es para usar anotaciones como @CreatedDate, @LastModifiedDate

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
