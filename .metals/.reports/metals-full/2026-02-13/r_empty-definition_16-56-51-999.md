error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/config/JpaConfig.java:org/springframework/security/crypto/password/PasswordEncoder#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/config/JpaConfig.java
empty definition using pc, found symbol in pc: org/springframework/security/crypto/password/PasswordEncoder#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 350
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/config/JpaConfig.java
text:
```scala
package com.Martin.SaaS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.@@PasswordEncoder;

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

```


#### Short summary: 

empty definition using pc, found symbol in pc: org/springframework/security/crypto/password/PasswordEncoder#