package com.Martin.SaaS.model;

import com.Martin.SaaS.model.enums.TipoMetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.YearMonth;

/**
 * Entidad PagoTarjeta - Método de pago con tarjeta de crédito/débito.
 * Extiende de MetodoPago usando herencia JOINED.
 */
@Entity
@Table(name = "pagos_tarjeta")
@DiscriminatorValue("TARJETA")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PagoTarjeta extends MetodoPago {

    /** Últimos 4 dígitos de la tarjeta (nunca almacenar el número completo) */
    @Column(name = "ultimos_digitos", nullable = false, length = 4)
    @Pattern(regexp = "\\d{4}", message = "Deben ser 4 dígitos")
    private String ultimosDigitos;

    @Column(name = "nombre_titular", nullable = false, length = 200)
    private String nombreTitular;

    @Column(name = "mes_expiracion", nullable = false)
    private Integer mesExpiracion;

    @Column(name = "anio_expiracion", nullable = false)
    private Integer anioExpiracion;

    @Column(name = "marca_tarjeta", length = 50)
    private String marcaTarjeta; // VISA, MASTERCARD, AMEX, etc.

    /** Token de la pasarela de pago (Stripe, PayPal, etc.) */
    @Column(name = "token_pago", length = 500)
    private String tokenPago;

    @Column(name = "es_debito")
    @Builder.Default
    private Boolean esDebito = false;

    @Override
    public String getInfoEnmascarada() {
        String marca = marcaTarjeta != null ? marcaTarjeta : "Tarjeta";
        return marca + " terminada en " + ultimosDigitos;
    }

    @Override
    public boolean esValido() {
        if (mesExpiracion == null || anioExpiracion == null) {
            return false;
        }
        YearMonth expiracion = YearMonth.of(anioExpiracion, mesExpiracion);
        return expiracion.isAfter(YearMonth.now()) || expiracion.equals(YearMonth.now());
    }

    /**
     * Verifica si la tarjeta está próxima a expirar (dentro de 30 días).
     * @return true si expira pronto
     */
    public boolean proximaAExpirar() {
        if (mesExpiracion == null || anioExpiracion == null) {
            return false;
        }
        YearMonth expiracion = YearMonth.of(anioExpiracion, mesExpiracion);
        YearMonth limite = YearMonth.now().plusMonths(1);
        return !expiracion.isAfter(limite);
    }

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (this.getTipo() == null) {
            this.setTipo(esDebito ? TipoMetodoPago.TARJETA_DEBITO : TipoMetodoPago.TARJETA_CREDITO);
        }
    }
}
