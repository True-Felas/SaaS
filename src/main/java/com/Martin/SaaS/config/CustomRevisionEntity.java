package com.Martin.SaaS.config;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Entidad de revisión personalizada para Envers.
 * Extiende la información de auditoría por defecto.
 */
@Entity
@Table(name = "revinfo")
@RevisionEntity(CustomRevisionListener.class)
@Getter
@Setter
public class CustomRevisionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private Long rev;

    @RevisionTimestamp
    @Column(name = "revtstmp")
    private Long timestamp;

    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * Obtiene la fecha y hora de la revisión como LocalDateTime.
     * @return LocalDateTime de la revisión
     */
    public LocalDateTime getFechaRevision() {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
}
