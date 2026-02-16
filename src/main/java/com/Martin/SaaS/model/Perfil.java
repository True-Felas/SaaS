package com.Martin.SaaS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

// Entidad Perfil - Información adicional del usuario. Relación One-to-One con Usuario.
@Entity
@Table(name = "perfiles")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false, unique = true)
	private Usuario usuario;

	@Column(length = 20)
	private String telefono;

	@Column(length = 200)
	private String direccion;

	@Column(length = 100)
	private String ciudad;

	@Column(length = 100)
	private String pais;

	@Column(name = "codigo_postal", length = 20)
	private String codigoPostal;

	@Column(name = "nombre_empresa", length = 200)
	private String nombreEmpresa;

	@Column(name = "nif_cif", length = 20)
	private String nifCif;

	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;

	@Column(length = 500)
	@Size(max = 500)
	private String bio;

	@Column(name = "avatar_url", length = 500)
	private String avatarUrl;

	@Column(name = "zona_horaria", length = 50)
	private String zonaHoraria;

	@Column(name = "idioma_preferido", length = 10)
	@Builder.Default
	private String idiomaPreferido = "es";

	@Column(name = "notificaciones_email")
	@Builder.Default
	private Boolean notificacionesEmail = true;

	@Column(name = "notificaciones_sms")
	@Builder.Default
	private Boolean notificacionesSms = false;
}
