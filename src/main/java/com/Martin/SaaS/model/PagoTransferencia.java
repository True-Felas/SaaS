package com.Martin.SaaS.model;

import com.Martin.SaaS.model.enums.TipoMetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * Entidad PagoTransferencia - Método de pago mediante transferencia bancaria.
 * Extiende de MetodoPago usando herencia JOINED.
 */
@Entity
@Table(name = "pagos_transferencia")
@DiscriminatorValue("TRANSFERENCIA")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PagoTransferencia extends MetodoPago {

    @Column(nullable = false, length = 34)
    @Pattern(regexp = "[A-Z]{2}\\d{2}[A-Z0-9]{1,30}", message = "IBAN inválido")
    private String iban;

    @Column(length = 11)
    @Pattern(regexp = "[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?", message = "BIC/SWIFT inválido")
    private String bic;

    @Column(name = "nombre_titular", nullable = false, length = 200)
    private String nombreTitular;

    @Column(name = "nombre_banco", length = 200)
    private String nombreBanco;

    @Column(name = "cuenta_verificada")
    @Builder.Default
    private Boolean cuentaVerificada = false;

    /** Referencia para el mandato SEPA */
    @Column(name = "mandato_sepa", length = 100)
    private String mandatoSepa;

    @Override
    public String getInfoEnmascarada() {
        if (iban == null || iban.length() < 8) {
            return "Transferencia Bancaria";
        }
        return "IBAN: " + iban.substring(0, 4) + " **** **** " + iban.substring(iban.length() - 4);
    }

    @Override
    public boolean esValido() {
        return iban != null && 
               !iban.isEmpty() && 
               nombreTitular != null && 
               !nombreTitular.isEmpty();
    }

    /**
     * Valida el formato básico del IBAN.
     * @return true si el formato es válido
     */
    public boolean validarFormatoIban() {
        if (iban == null || iban.length() < 15 || iban.length() > 34) {
            return false;
        }
        return iban.matches("[A-Z]{2}\\d{2}[A-Z0-9]{1,30}");
    }

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        this.setTipo(TipoMetodoPago.TRANSFERENCIA_BANCARIA);
    }
}
