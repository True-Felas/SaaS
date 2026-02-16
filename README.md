# SaaS Project - Sistema de Gestión de Suscripciones

Sistema robusto para gestión de suscripciones SaaS con soporte para diferentes planes, facturación automática, cálculo de impuestos y auditoría de cambios.

## Características Principales

*   **Gestión de Planes**: Basic, Premium, Enterprise con jerarquía y características.
*   **Suscripciones**: Ciclo de vida completo (Activa, Pausada, Cancelada, Morosa).
*   **Facturación Automática**: Generación mensual y prorrateo por cambios de plan.
*   **Impuestos**: Cálculo automático basado en el país del usuario (ES, US, MX, AR, CO).
*   **Auditoría (Envers)**: Historial completo de cambios en suscripciones y planes.
*   **Seguridad**: RBAC (Role-Based Access Control) con separación estricta:
    *   **Admin**: Gestión global y acceso a auditoría.
    *   **Usuario**: Gestión exclusiva de su cuenta y facturas.

## Tecnologías

*   **Backend**: Java 21, Spring Boot 3.x, Spring Data JPA
*   **Base de Datos**: H2 (Desarrollo), MySQL (Producción)
*   **Auditoría**: Hibernate Envers
*   **Frontend**: React + Vite (en carpeta `frontend`)
*   **Build**: Maven

## Requisitos Previos

*   Java 21 JDK
*   Maven 3.8+
*   Node.js 18+ (para el frontend)

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

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| POST | `/api/suscripciones` | Crear suscripción | Admin / Owner |
| POST | `/api/suscripciones/cambiar-plan` | Upgrade/Downgrade | Admin / Owner |
| GET | `/api/admin/auditoria` | Ver historial de cambios | **Admin Only** |
| GET | `/api/admin/usuarios` | Listar todos los usuarios | **Admin Only** |

## Estructura del Proyecto

*   `model`: Entidades JPA (`Usuario`, `Suscripcion`, `Plan`, `Factura`).
*   `repository`: Repositorios Spring Data.
*   `service`: Lógica de negocio (Renovación, Impuestos, Auditoría).
*   `controller`: Endpoints REST con validación de seguridad.
*   `config`: Configuración de Beans y carga de datos iniciales.
