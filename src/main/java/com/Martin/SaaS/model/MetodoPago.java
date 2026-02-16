package com.Martin.SaaS.model;

import com.Martin.SaaS.model.enums.TipoMetodoPago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

// Entidad base MetodoPago - Clase abstracta para diferentes tipos de pago. Usa estrategia de herencia JOINED para tablas separadas.
@Entity
@Table(name = "metodos_pago")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_pago", discriminatorType = DiscriminatorType.STRING)
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class MetodoPago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	private TipoMetodoPago tipo;

	@Column(nullable = false)
	@Builder.Default
	private Boolean activo = true;

	@Column(name = "es_principal")
	@Builder.Default
	private Boolean esPrincipal = false;

	@Column(length = 100)
	private String alias;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;

	@PrePersist
	protected void onCreate() {
		this.fechaCreacion = LocalDateTime.now();
		this.fechaModificacion = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.fechaModificacion = LocalDateTime.now();
	}

	// Método abstracto para obtener información enmascarada del método de pago.
	// @return String con la información del pago parcialmente oculta
	public abstract String getInfoEnmascarada();

	// Método abstracto para verificar si el método de pago es válido. @return true
	// si es válido
	public abstract boolean esValido();
}
