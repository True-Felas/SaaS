error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Usuario.java:com/Martin/SaaS/model/enums/Role#USER#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Usuario.java
empty definition using pc, found symbol in pc: com/Martin/SaaS/model/enums/Role#USER#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1432
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Usuario.java
text:
```scala
package com.Martin.SaaS.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Usuario - Representa a un usuario registrado en el sistema SaaS.
 * Auditada con Envers para tracking de cambios.
 */
@Entity
@Table(name = "usuarios")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "email_verificado")
    @Builder.Default
    private Boolean emailVerificado = false;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private com.Martin.SaaS.model.enums.Role role = com.Martin.SaaS.model.enums.Role.@@USER;

    // Relación One-to-One con Perfil
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Perfil perfil;

    // Relación One-to-Many con Suscripciones (historial)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Suscripcion> suscripciones = new ArrayList<>();

    // Relación One-to-Many con Métodos de Pago
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MetodoPago> metodosPago = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }

    /**
     * Obtiene la suscripción activa del usuario.
     * @return La suscripción activa o null si no tiene ninguna
     */
    public Suscripcion getSuscripcionActiva() {
        return suscripciones.stream()
                .filter(s -> s.getEstado() == com.Martin.SaaS.model.enums.EstadoSuscripcion.ACTIVA 
 s.getEstado() == com.Martin.SaaS.model.enums.EstadoSuscripcion.TRIAL)
                .findFirst()
                .orElse(null);
    }

    /**
     * Añade una suscripción al usuario.
     * @param suscripcion La suscripción a añadir
     */
    public void addSuscripcion(Suscripcion suscripcion) {
        suscripciones.add(suscripcion);
        suscripcion.setUsuario(this);
    }

    /**
     * Añade un método de pago al usuario.
     * @param metodoPago El método de pago a añadir
     */
    public void addMetodoPago(MetodoPago metodoPago) {
        metodosPago.add(metodoPago);
        metodoPago.setUsuario(this);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/Martin/SaaS/model/enums/Role#USER#