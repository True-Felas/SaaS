# SaaS Project - Sistema de Gestión de Suscripciones

Sistema robusto para gestión de suscripciones SaaS con soporte para diferentes planes, facturación automática, cálculo de impuestos y auditoría de cambios.

## Características Principales

- **Gestión de Planes**: Basic, Premium, Enterprise con jerarquía y características.
- **Suscripciones**: Ciclo de vida completo (Activa, Pausada, Cancelada, Morosa).
- **Facturación Automática**: Generación mensual y prorrateo por cambios de plan.
- **Impuestos**: Cálculo automático basado en el país del usuario (ES, US, MX, AR, CO).
- **Auditoría (Envers)**: Historial completo de cambios en suscripciones y planes.
- **Seguridad**: RBAC (Role-Based Access Control) con separación estricta:
  - **Admin**: Gestión global y acceso a auditoría.
  - **Usuario**: Gestión exclusiva de su cuenta y facturas.

## Tecnologías

- **Backend**: Java 21, Spring Boot 3.x, Spring Data JPA
- **Base de Datos**: H2 (Desarrollo), MySQL (Producción)
- **Auditoría**: Hibernate Envers
- **Frontend**: React + Vite (en carpeta `frontend`)
- **Build**: Maven

## Requisitos Previos

- Java 21 JDK
- Maven 3.8+
- Node.js 18+ (para el frontend)

## Instalación y Ejecución

### Backend

1.  Clonar el repositorio.
2.  Navegar a la carpeta raíz `SaaS`.
3.  Ejecutar:
    ```bash
    ./mvnw spring-boot:run
    ```
    El servidor iniciará en `http://localhost:8030`.
    Acceso a consola H2: `http://localhost:8030/h2-console` (URL JDBC: `jdbc:h2:file:./data/saasdb`)

### Frontend

1.  Navegar a `frontend`.
2.  Instalar dependencias:
    ```bash
    npm install
    ```
3.  Iniciar servidor de desarrollo:
    ```bash
    npm run dev
    ```

## Testing

Para ejecutar las pruebas unitarias:

```bash
./mvnw test
```

## API Endpoints Notables

| Método | Endpoint                                   | Descripción                       | Acceso         |
| :----- | :----------------------------------------- | :-------------------------------- | :------------- |
| POST   | `/api/usuarios/registro`                   | Registrar nuevo usuario           | Público        |
| POST   | `/api/usuarios/login`                      | Autenticación                     | Público        |
| GET    | `/api/planes`                              | Listar planes disponibles         | Público        |
| POST   | `/api/suscripciones`                       | Crear suscripción                 | Admin / Owner  |
| POST   | `/api/suscripciones/cambiar-plan`          | Upgrade/Downgrade de plan         | Admin / Owner  |
| POST   | `/api/suscripciones/{id}/cancelar`         | Cancelar suscripción              | Admin / Owner  |
| GET    | `/api/suscripciones/usuario/{id}/facturas` | Ver facturas de usuario           | Admin / Owner  |
| GET    | `/api/admin/auditoria`                     | Ver historial de cambios (Envers) | **Admin Only** |
| GET    | `/api/admin/usuarios`                      | Listar todos los usuarios         | **Admin Only** |
| GET    | `/api/admin/stats`                         | Estadísticas globales             | **Admin Only** |
| GET    | `/api/admin/suscripciones`                 | Todas las suscripciones           | **Admin Only** |

## Credenciales por Defecto (Datos de Prueba)

Al arrancar, el `DataLoader` crea automáticamente:

| Usuario       | Contraseña | Rol   |
| :------------ | :--------- | :---- |
| `admin@local` | `admin123` | ADMIN |

Los 3 planes (Basic €9.99, Premium €29.99, Enterprise €99.99) también se crean automáticamente.

## Pruebas Unitarias

Ejecutar con `./mvnw test`. Resultados del último ciclo:

| Clase de Prueba          | Casos  | Resultado    |
| :----------------------- | :----: | :----------- |
| `SuscripcionServiceTest` |   7    | PASS         |
| `PlanServiceTest`        |   2    | PASS         |
| `UsuarioServiceTest`     |   2    | PASS         |
| **Total**                | **11** | **0 fallos** |

### Detalle de Casos de Prueba

| #   | Caso de Prueba                                                 | Descripción                                                | Resultado | Corrección |
| :-- | :------------------------------------------------------------- | :--------------------------------------------------------- | :-------- | :--------- |
| 1   | `crearSuscripcion_DeberiaCrearSuscripcionYFactura`             | Crea suscripción y genera la primera factura               | PASS      | -          |
| 2   | `generarFacturaMensual_DeberiaCalcularImpuestoEspana`          | Impuesto ES = 21%, total = 12.10 para plan 10              | PASS      | -          |
| 3   | `generarFacturaMensual_DeberiaCalcularImpuestoUSA`             | Impuesto US = 8%, total = 10.80 para plan 10               | PASS      | -          |
| 4   | `cambiarPlan_UpgradeDeberiaGenerarProrrateo`                   | Upgrade Basic a Premium genera factura PRORRATEO           | PASS      | -          |
| 5   | `cancelarSuscripcion_DeberiaCambiarEstadoACancelada`           | Estado cambia a CANCELADA con motivo                       | PASS      | -          |
| 6   | `pagarFactura_DeberiaMarcarComoPagada`                         | Estado cambia a PAGADA y se registra fecha de pago         | PASS      | -          |
| 7   | `procesarCobrosAutomaticos_DeberiaRenovarSuscripcionesActivas` | Job de renovación genera factura y actualiza próximo cobro | PASS      | -          |
| 8   | `obtenerPlanesActivos_DeberiaRetornarSoloActivos`              | Solo retorna planes con `activo=true`                      | PASS      | -          |
| 9   | `obtenerPorId_DeberiaRetornarPlan`                             | Busca plan por ID correctamente                            | PASS      | -          |
| 10  | `buscarPorEmail_DeberiaRetornarUsuario`                        | Encuentra usuario por email                                | PASS      | -          |
| 11  | `existeEmail_DeberiaRetornarTrue`                              | Verifica existencia de email duplicado                     | PASS      | -          |

## Estructura del Proyecto

- `model`: Entidades JPA (`Usuario`, `Suscripcion`, `Plan`, `Factura`, `MetodoPago` y subclases).
- `repository`: Repositorios Spring Data con queries personalizadas.
- `service`: Lógica de negocio (`SuscripcionService`, `UsuarioService`, `PlanService`, `AuditService`, `SecurityService`).
- `controller`: Endpoints REST limpios, toda lógica de autorización delegada a `SecurityService`.
- `config`: `DataLoader` (datos iniciales), `CustomRevisionEntity` (auditoría Envers).
