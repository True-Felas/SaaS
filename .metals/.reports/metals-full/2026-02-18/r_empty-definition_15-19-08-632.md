error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Suscripcion.java:com/Martin/SaaS/model/enums/EstadoSuscripcion#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Suscripcion.java
empty definition using pc, found symbol in pc: com/Martin/SaaS/model/enums/EstadoSuscripcion#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 69
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Suscripcion.java
text:
```scala
package com.Martin.SaaS.model;

import com.Martin.SaaS.model.enums.@@EstadoSuscripcion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// Entidad Suscripción - Representa la suscripción de un usuario a un plan. Auditada con Envers para tracking completo de cambios de plan.
@Entity
@Table(name = "suscripciones")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suscripcion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_id", nullable = false)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Plan plan;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private EstadoSuscripcion estado = EstadoSuscripcion.ACTIVA;

	@Column(name = "fecha_inicio", nullable = false)
	private LocalDate fechaInicio;

	@Column(name = "fecha_fin")
	private LocalDate fechaFin;

	@Column(name = "fecha_proximo_cobro")
	private LocalDate fechaProximoCobro;

	@Column(name = "fecha_ultimo_cobro")
	private LocalDate fechaUltimoCobro;

	// Indica si la renovación automática está activa
	@Column(name = "auto_renovacion")
	@Builder.Default
	private Boolean autoRenovacion = true;

	// Días del periodo de facturación (normalmente 30)
	@Column(name = "periodo_facturacion_dias")
	@Builder.Default
	private Integer periodoFacturacionDias = 30;

	// Días de prueba restantes (para planes TRIAL)
	@Column(name = "dias_prueba")
	@Builder.Default
	private Integer diasPrueba = 0;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;

	@Column(name = "motivo_cancelacion", length = 500)
	private String motivoCancelacion;

	// Relación One-to-Many con Facturas
	@OneToMany(mappedBy = "suscripcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Builder.Default
	private List<Factura> facturas = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		this.fechaCreacion = LocalDateTime.now();
		this.fechaModificacion = LocalDateTime.now();
		if (this.fechaInicio == null) {
			this.fechaInicio = LocalDate.now();
		}
		if (this.fechaProximoCobro == null) {
			this.fechaProximoCobro = this.fechaInicio.plusDays(periodoFacturacionDias);
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.fechaModificacion = LocalDateTime.now();
	}

	// Añade una factura a la suscripción. @param factura La factura a añadir
	public void addFactura(Factura factura) {
		facturas.add(factura);
		factura.setSuscripcion(this);
	}

	// Calcula los días restantes hasta el próximo cobro. @return Número de días
	// hasta el próximo cobro
	public long getDiasHastaProximoCobro() {
		if (fechaProximoCobro == null) {
			return 0;
		}
		return ChronoUnit.DAYS.between(LocalDate.now(), fechaProximoCobro);
	}

	// Calcula los días transcurridos desde el último cobro. @return Número de días
	// desde el último cobro
	public long getDiasDesdeUltimoCobro() {
		LocalDate referencia = fechaUltimoCobro != null ? fechaUltimoCobro : fechaInicio;
		return ChronoUnit.DAYS.between(referencia, LocalDate.now());
	}

	// Verifica si la suscripción está activa. @return true si está activa o en
	// trial
	public boolean estaActiva() {
		return estado == EstadoSuscripcion.ACTIVA || estado == EstadoSuscripcion.TRIAL;
	}

	// Verifica si es momento de generar una nueva factura. @return true si la fecha
	// actual es igual o posterior a la fecha de próximo cobro
	public boolean debeCobrar() {
		return estaActiva() && fechaProximoCobro != null && !LocalDate.now().isBefore(fechaProximoCobro);
	}

	// Cancela la suscripción con un motivo. @param motivo Motivo de la cancelación
	public void cancelar(String motivo) {
		this.estado = EstadoSuscripcion.CANCELADA;
		this.motivoCancelacion = motivo;
		this.fechaFin = LocalDate.now();
		this.autoRenovacion = false;
	}

	// Pausa la suscripción.
	public void pausar() {
		this.estado = EstadoSuscripcion.PAUSADA;
	}

	// Reactiva una suscripción pausada.
	public void reactivar() {
		if (this.estado == EstadoSuscripcion.PAUSADA) {
			this.estado = EstadoSuscripcion.ACTIVA;
		}
	}

	// Marca la suscripción como morosa.
	public void marcarMorosa() {
		this.estado = EstadoSuscripcion.MOROSA;
	}
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/Martin/SaaS/model/enums/EstadoSuscripcion#