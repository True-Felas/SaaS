error id: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Factura.java:_empty_/ManyToOne#fetch#
file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Factura.java
empty definition using pc, found symbol in pc: _empty_/ManyToOne#fetch#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 914
uri: file:///C:/Users/juanx/Desktop/SpringBoot/SaaS/SaaS/src/main/java/com/Martin/SaaS/model/Factura.java
text:
```scala
package com.Martin.SaaS.model;

import com.Martin.SaaS.model.enums.EstadoFactura;
import com.Martin.SaaS.model.enums.TipoFactura;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Entidad Factura - Representa una factura generada por una suscripción. Se genera automáticamente cada 30 días o por cambios de plan (prorrateo).
@Entity
@Table(name = "facturas")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numero_factura", nullable = false, unique = true, length = 50)
	private String numeroFactura;

	@ManyToOne(@@fetch = FetchType.LAZY)
	@JoinColumn(name = "suscripcion_id", nullable = false)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Suscripcion suscripcion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "metodo_pago_id")
	private MetodoPago metodoPago;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private TipoFactura tipo = TipoFactura.MENSUAL;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private EstadoFactura estado = EstadoFactura.PENDIENTE;

	@NotNull
	@DecimalMin(value = "0.00")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal subtotal;

	@DecimalMin(value = "0.00")
	@Column(precision = 10, scale = 2)
	@Builder.Default
	private BigDecimal descuento = BigDecimal.ZERO;

	@DecimalMin(value = "0.00")
	@Column(name = "porcentaje_impuesto", precision = 5, scale = 2)
	@Builder.Default
	private BigDecimal porcentajeImpuesto = new BigDecimal("21.00"); // IVA España

	@DecimalMin(value = "0.00")
	@Column(precision = 10, scale = 2)
	private BigDecimal impuesto;

	@NotNull
	@DecimalMin(value = "0.00")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal total;

	@Column(name = "periodo_inicio")
	private LocalDate periodoInicio;

	@Column(name = "periodo_fin")
	private LocalDate periodoFin;

	@Column(name = "fecha_emision", nullable = false)
	private LocalDate fechaEmision;

	@Column(name = "fecha_vencimiento")
	private LocalDate fechaVencimiento;

	@Column(name = "fecha_pago")
	private LocalDate fechaPago;

	@Column(length = 500)
	private String concepto;

	@Column(length = 1000)
	private String notas;

	// Para facturas de prorrateo: días prorrateados
	@Column(name = "dias_prorrateados")
	private Integer diasProrrateados;

	// Para facturas de prorrateo: referencia al plan anterior
	@Column(name = "plan_anterior", length = 100)
	private String planAnterior;

	// Para facturas de prorrateo: referencia al plan nuevo
	@Column(name = "plan_nuevo", length = 100)
	private String planNuevo;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;

	@PrePersist
	protected void onCreate() {
		this.fechaCreacion = LocalDateTime.now();
		this.fechaModificacion = LocalDateTime.now();
		if (this.fechaEmision == null) {
			this.fechaEmision = LocalDate.now();
		}
		if (this.fechaVencimiento == null) {
			this.fechaVencimiento = this.fechaEmision.plusDays(30);
		}
		calcularTotales();
		generarNumeroFactura();
	}

	@PreUpdate
	protected void onUpdate() {
		this.fechaModificacion = LocalDateTime.now();
		calcularTotales();
	}

	// Calcula el impuesto y el total de la factura.
	public void calcularTotales() {
		if (subtotal == null) {
			subtotal = BigDecimal.ZERO;
		}
		if (descuento == null) {
			descuento = BigDecimal.ZERO;
		}
		if (porcentajeImpuesto == null) {
			porcentajeImpuesto = new BigDecimal("21.00");
		}

		BigDecimal baseImponible = subtotal.subtract(descuento);
		this.impuesto = baseImponible.multiply(porcentajeImpuesto).divide(new BigDecimal("100"), 2,
				java.math.RoundingMode.HALF_UP);
		this.total = baseImponible.add(this.impuesto);
	}

	// Genera un número de factura único.
	private void generarNumeroFactura() {
		if (this.numeroFactura == null || this.numeroFactura.isEmpty()) {
			String year = String.valueOf(LocalDate.now().getYear());
			String timestamp = String.valueOf(System.currentTimeMillis()).substring(5);
			this.numeroFactura = "FAC-" + year + "-" + timestamp;
		}
	}

	// Marca la factura como pagada.
	public void marcarComoPagada() {
		this.estado = EstadoFactura.PAGADA;
		this.fechaPago = LocalDate.now();
	}

	// Marca la factura como vencida.
	public void marcarComoVencida() {
		if (this.estado == EstadoFactura.PENDIENTE) {
			this.estado = EstadoFactura.VENCIDA;
		}
	}

	// Cancela la factura.
	public void cancelar() {
		this.estado = EstadoFactura.CANCELADA;
	}

	// Verifica si la factura está vencida. @return true si está pendiente y la
	// fecha de vencimiento ha pasado
	public boolean estaVencida() {
		return estado == EstadoFactura.PENDIENTE && fechaVencimiento != null
				&& LocalDate.now().isAfter(fechaVencimiento);
	}

	// Verifica si la factura está pagada. @return true si está pagada
	public boolean estaPagada() {
		return estado == EstadoFactura.PAGADA;
	}
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/ManyToOne#fetch#