# Diagrama Entidad-Relación - Plataforma SaaS

## Descripción General

Este documento describe el modelo de datos de la plataforma SaaS, incluyendo las entidades principales, sus relaciones y los atributos clave.

## Diagrama E-R (Mermaid)

```mermaid
erDiagram
    USUARIO ||--o| PERFIL : tiene
    USUARIO ||--o{ SUSCRIPCION : tiene
    USUARIO ||--o{ METODO_PAGO : tiene
    PLAN ||--o{ SUSCRIPCION : tiene
    SUSCRIPCION ||--o{ FACTURA : genera
    METODO_PAGO ||--o{ FACTURA : paga

    USUARIO {
        Long id PK
        String email UK
        String password
        String nombre
        String apellido
        Boolean activo
        Boolean emailVerificado
        DateTime fechaRegistro
        DateTime ultimoAcceso
    }

    PERFIL {
        Long id PK
        Long usuario_id FK
        String telefono
        String direccion
        String ciudad
        String pais
        String codigoPostal
        String nombreEmpresa
        String nifCif
        Date fechaNacimiento
        String bio
        String avatarUrl
        String zonaHoraria
        String idiomaPreferido
        Boolean notificacionesEmail
        Boolean notificacionesSms
    }

    PLAN {
        Long id PK
        TipoPlan tipo UK
        String nombre
        String descripcion
        Decimal precioMensual
        Decimal precioAnual
        Integer maxUsuarios
        Integer almacenamientoGb
        Integer maxProyectos
        Boolean soportePrioritario
        Boolean apiAcceso
        Boolean analyticsAvanzados
        Boolean integracionesExternas
        Boolean activo
        DateTime fechaCreacion
        DateTime fechaModificacion
    }

    SUSCRIPCION {
        Long id PK
        Long usuario_id FK
        Long plan_id FK
        EstadoSuscripcion estado
        Date fechaInicio
        Date fechaFin
        Date fechaProximoCobro
        Date fechaUltimoCobro
        Boolean autoRenovacion
        Integer periodoFacturacionDias
        Integer diasPrueba
        String motivoCancelacion
        DateTime fechaCreacion
        DateTime fechaModificacion
    }

    FACTURA {
        Long id PK
        String numeroFactura UK
        Long suscripcion_id FK
        Long metodoPago_id FK
        TipoFactura tipo
        EstadoFactura estado
        Decimal subtotal
        Decimal descuento
        Decimal porcentajeImpuesto
        Decimal impuesto
        Decimal total
        Date periodoInicio
        Date periodoFin
        Date fechaEmision
        Date fechaVencimiento
        Date fechaPago
        String concepto
        String notas
        Integer diasProrrateados
        String planAnterior
        String planNuevo
        DateTime fechaCreacion
        DateTime fechaModificacion
    }

    METODO_PAGO {
        Long id PK
        Long usuario_id FK
        TipoMetodoPago tipo
        Boolean activo
        Boolean esPrincipal
        String alias
        DateTime fechaCreacion
        DateTime fechaModificacion
    }

    PAGO_TARJETA {
        Long id PK,FK
        String ultimosDigitos
        String nombreTitular
        Integer mesExpiracion
        Integer anioExpiracion
        String marcaTarjeta
        String tokenPago
        Boolean esDebito
    }

    PAGO_PAYPAL {
        Long id PK,FK
        String emailPaypal
        String billingAgreementId
        String payerId
        Boolean cuentaVerificada
        DateTime fechaAutorizacion
    }

    PAGO_TRANSFERENCIA {
        Long id PK,FK
        String iban
        String bic
        String nombreTitular
        String nombreBanco
        Boolean cuentaVerificada
        String mandatoSepa
    }

    METODO_PAGO ||--o| PAGO_TARJETA : es
    METODO_PAGO ||--o| PAGO_PAYPAL : es
    METODO_PAGO ||--o| PAGO_TRANSFERENCIA : es
```

## Enumeraciones

### EstadoSuscripcion

| Valor     | Descripción                                       |
| --------- | ------------------------------------------------- |
| ACTIVA    | Suscripción activa y al día con los pagos         |
| CANCELADA | Suscripción cancelada por el usuario o el sistema |
| MOROSA    | Suscripción con pagos pendientes                  |
| PAUSADA   | Suscripción pausada temporalmente                 |
| TRIAL     | Suscripción en periodo de prueba                  |

### TipoPlan

| Valor      | Nivel | Descripción                  |
| ---------- | ----- | ---------------------------- |
| BASIC      | 1     | Plan Básico - €9.99/mes      |
| PREMIUM    | 2     | Plan Premium - €29.99/mes    |
| ENTERPRISE | 3     | Plan Enterprise - €99.99/mes |

### EstadoFactura

| Valor       | Descripción                             |
| ----------- | --------------------------------------- |
| PENDIENTE   | Factura generada pero pendiente de pago |
| PAGADA      | Factura pagada correctamente            |
| VENCIDA     | Factura vencida sin pagar               |
| CANCELADA   | Factura cancelada                       |
| REEMBOLSADA | Factura reembolsada                     |

### TipoFactura

| Valor     | Descripción                              |
| --------- | ---------------------------------------- |
| MENSUAL   | Factura mensual regular                  |
| PRORRATEO | Factura por prorrateo al cambiar de plan |
| AJUSTE    | Factura de ajuste o corrección           |
| REEMBOLSO | Nota de crédito                          |

### TipoMetodoPago

| Valor                  | Descripción            |
| ---------------------- | ---------------------- |
| TARJETA_CREDITO        | Tarjeta de Crédito     |
| TARJETA_DEBITO         | Tarjeta de Débito      |
| PAYPAL                 | PayPal                 |
| TRANSFERENCIA_BANCARIA | Transferencia Bancaria |

## Estrategias de Herencia

### MetodoPago (JOINED)

La entidad `MetodoPago` utiliza la estrategia de herencia **JOINED**, lo que significa:

- Existe una tabla base `metodos_pago` con los atributos comunes
- Cada subtipo tiene su propia tabla (`pagos_tarjeta`, `pagos_paypal`, `pagos_transferencia`)
- Las tablas hijas se relacionan con la tabla padre mediante clave foránea

**Ventajas:**

- Normalización de datos
- Sin columnas nulas innecesarias
- Facilita añadir nuevos tipos de pago

## Auditoría con Hibernate Envers

Todas las entidades principales están anotadas con `@Audited`, lo que genera:

- Tablas de auditoría con sufijo `_AUD`
- Registro de cada modificación con número de revisión
- Información del usuario que realizó el cambio (mediante `CustomRevisionEntity`)

### Tablas de Auditoría Generadas

- `usuarios_AUD`
- `perfiles_AUD`
- `planes_AUD`
- `suscripciones_AUD`
- `facturas_AUD`
- `metodos_pago_AUD`
- `pagos_tarjeta_AUD`
- `pagos_paypal_AUD`
- `pagos_transferencia_AUD`
- `revinfo` (tabla de revisiones)

## Reglas de Negocio Implementadas

1. **Generación automática de facturas**: Cada 30 días (configurable por `periodoFacturacionDias`)
2. **Prorrateo en upgrades**: Cuando un usuario cambia a un plan más caro, se genera una factura de prorrateo
3. **Un usuario solo puede tener una suscripción activa a la vez**
4. **Las facturas incluyen IVA del 21% por defecto**
5. **Los números de factura se generan automáticamente**
