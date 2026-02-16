package com.Martin.SaaS.model;

import com.Martin.SaaS.model.enums.TipoMetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

// Entidad PagoPayPal - Método de pago mediante cuenta PayPal. Extiende de MetodoPago usando herencia JOINED.
@Entity
@Table(name = "pagos_paypal")
@DiscriminatorValue("PAYPAL")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PagoPayPal extends MetodoPago {

	@Email
	@Column(name = "email_paypal", nullable = false, length = 200)
	private String emailPaypal;

	// ID de acuerdo de facturación de PayPal
	@Column(name = "billing_agreement_id", length = 200)
	private String billingAgreementId;

	// ID del pagador en PayPal
	@Column(name = "payer_id", length = 200)
	private String payerId;

	@Column(name = "cuenta_verificada")
	@Builder.Default
	private Boolean cuentaVerificada = false;

	@Column(name = "fecha_autorizacion")
	private LocalDateTime fechaAutorizacion;

	@Override
	public String getInfoEnmascarada() {
		if (emailPaypal == null || emailPaypal.isEmpty()) {
			return "PayPal";
		}
		int atIndex = emailPaypal.indexOf('@');
		if (atIndex <= 2) {
			return "PayPal: ***" + emailPaypal.substring(atIndex);
		}
		return "PayPal: " + emailPaypal.substring(0, 2) + "***" + emailPaypal.substring(atIndex);
	}

	@Override
	public boolean esValido() {
		return emailPaypal != null && !emailPaypal.isEmpty() && billingAgreementId != null
				&& !billingAgreementId.isEmpty();
	}

	@PrePersist
	@Override
	protected void onCreate() {
		super.onCreate();
		this.setTipo(TipoMetodoPago.PAYPAL);
	}
}
