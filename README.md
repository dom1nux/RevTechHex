# RevTech — Plataforma Integral de Inspección Técnica Vehicular

Plataforma basada en **Arquitectura de Microservicios** y **Domain-Driven Design (DDD)** para la gestión y certificación técnica vehicular conforme a la normativa regulatoria.

---

## 1. Arquitectura y Dominio

El sistema se estructura en contextos delimitados (*Bounded Contexts*) siguiendo el documento de referencia [**DISEÑO TÁCTICO - REVTECH.md**](docs/DISEÑO%20TÁCTICO%20-%20REVTECH.md):

* **API Gateway & Punto de Entrada Central:**
  * [`msvc-gateway`](msvc-gateway/): Punto de entrada único para clientes SPA, aplicaciones móviles y consumidores externos (Puerto 8000). Centraliza el enrutamiento inteligente, CORS global y el portal unificado de documentación interactiva [Scalar API Reference](http://localhost:8000/scalar).
* **Subdominio Core:**
  * [`msvc-inspection`](msvc-inspection/): Núcleo del negocio (Puerto 8080). Gestiona el ciclo de vida de la inspección técnica vehicular, registro de pruebas por sistemas (frenos, emisiones, suspensión, dirección, etc.) y la emisión reglamentaria mutuamente excluyente del **Certificado de Inspección** (Apto) o **Acta de Observaciones** (Observado).
* **Subdominios de Soporte / Genéricos:**
  * `msvc-clientes`: Gestión de clientes y parque vehicular (Agregado `Vehiculo`, Puerto 8081).
  * `msvc-identidad`: Control de acceso y personal (inspectores, supervisores, Puerto 8082).
  * `msvc-citas`: Agendamiento y reserva de turnos de inspección.
  * `msvc-pagos`: Procesamiento y validación de transacciones de pago.
  * `msvc-administrativa`: Gestión de líneas de inspección y catálogos administrativos.

---

## 2. Pila Tecnológica

| Componente | Tecnología |
|---|---|
| **Lenguaje** | Java 26 (Oracle JDK 26+) |
| **Framework Base** | Spring Boot 4.1.1 |
| **Cloud & Integración** | Spring Cloud 2025.1.3 (OpenFeign) |
| **Persistencia** | Spring Data JPA / Hibernate |
| **Base de Datos** | MySQL 8.4 LTS (*Database per Service*) |
| **Documentación API** | OpenAPI 3 / Scalar API Reference (`scalar-webmvc` 0.6.74 + `springdoc-openapi` 3.1.0) |
| **Contenedores** | Docker & Docker Compose |

---

## 3. Requisitos Previos

* **Java Development Kit (JDK):** Versión 26 o superior instalada y configurada en `PATH`.
* **Base de datos, una de estas opciones:**
  * **Docker & Docker Compose:** levanta MySQL 8.4 LTS automáticamente (Modalidades A y B).
  * **MySQL instalado localmente:** 8.4 LTS recomendado, o cualquier 8.0.16+ (Modalidad C, sin Docker).
* **Git:** Para gestión de versiones y ramas.

---

## 4. Orquestación del Entorno de Desarrollo (`mise` y Docker)

RevTech cuenta con una interfaz unificada de tareas gestionada por [**mise**](https://mise.jdx.dev) (`mise.toml`). Puedes operar el sistema en dos modalidades según tu flujo de trabajo:

### Modalidad A: Desarrollo Local Enfocado (Host + Base de Datos en Docker)
Ideal para desarrollo activo en tu IDE con Java 26, depuración con breakpoints y reinicio rápido:

```powershell
# 1. Iniciar únicamente la base de datos MySQL 8.4 LTS
mise run db:up

# 2. Levantar el microservicio en el que vas a trabajar localmente
mise run dev msvc-inspection
# O cualquier otro servicio pasándolo como argumento:
mise run dev msvc-clientes
mise run dev msvc-identidad
mise run dev msvc-gateway
```

### Modalidad B: Plataforma Completa en Contenedores (E2E y Gateway)
Ideal para verificar la interoperabilidad entre microservicios, el API Gateway y el portal Scalar:

```powershell
# 1. Construir las imágenes OCI de los microservicios con Cloud Native Buildpacks
mise run app:build

# 2. Levantar toda la plataforma (BD + 3 Microservicios + API Gateway)
mise run app:up

# 3. Monitorear los logs unificados en tiempo real
mise run app:logs

# 4. Detener todo el sistema completo
mise run app:down
```

### Modalidad C: Sin Docker (MySQL instalado localmente)
Para ejecutar el proyecto con un MySQL instalado directamente en el equipo, sin Docker ni `mise`:

```powershell
# 1. Crear las bases de datos y usuarios (una sola vez; pide la contraseña de root)
mysql -u root -p < docker/mysql/init-databases.sql

# 2. Levantar un microservicio (Windows; en Linux/macOS usar ./mvnw)
mvnw.cmd spring-boot:run -pl msvc-inspection
```

Por defecto, los servicios se conectan a `localhost:3306` con los usuarios creados por el script. Si MySQL usa otro host o puerto, defínelo antes de levantar el servicio:

```powershell
$env:DB_HOST="localhost"; $env:DB_PORT="3307"
```

---

## 5. Referencia de Comandos Unificados (`mise`)

| Comando de `mise` | Comando Nativo Equivalente | Descripción |
|---|---|---|
| `mise run db:up` | `docker compose up -d mysql` | Inicia el contenedor MySQL 8.4 LTS |
| `mise run db:down` | `docker compose down` | Detiene el contenedor MySQL |
| `mise run db:logs` | `docker compose logs -f mysql` | Logs en vivo de MySQL |
| `mise run dev [servicio]` | `./mvnw spring-boot:run -pl :[servicio]` | Ejecuta un servicio en el host (default: `msvc-inspection`) |
| `mise run app:build` | `./mvnw spring-boot:build-image -DskipTests` | Construye imágenes OCI con Paketo Buildpacks |
| `mise run app:up` | `docker compose -f docker-compose.full.yml up -d` | Levanta todo el stack en Docker |
| `mise run app:down` | `docker compose -f docker-compose.full.yml down` | Detiene todo el stack |
| `mise run app:logs` | `docker compose -f docker-compose.full.yml logs -f` | Logs en vivo de todo el stack |
| `mise run app:restart` | `docker compose -f docker-compose.full.yml restart` | Reinicia todos los contenedores |
| `mise run compile` | `./mvnw clean compile` | Compila todos los módulos del reactor |
| `mise run package` | `./mvnw package -DskipTests` | Empaqueta JARs sin ejecutar pruebas |
| `mise run test` | `./mvnw test` | Ejecuta las pruebas unitarias |

---

## 6. Documentación Interactiva y API Gateway

Con la plataforma levantada (`mise run app:up` o `msvc-gateway` en el puerto `8000`):
* 👉 **Portal Scalar Unificado:** [http://localhost:8000/scalar](http://localhost:8000/scalar)
* **OpenAPI Unificado:** [http://localhost:8000/v3/api-docs/all](http://localhost:8000/v3/api-docs/all)
* Para la guía de ejecución aislada del núcleo técnico, consulta el [**README interno de msvc-inspection**](msvc-inspection/README.md).

---

## 7. Flujo de Trabajo y Ramas (`gh-stack`)

* **Rama principal:** `master`.
* **Commits:** Formato *Conventional Commits* en inglés (`feat:`, `fix:`, `refactor:`, `build:`, `docs:`, `chore:`).
* **Stacked PRs:** Se gestionan mediante la extensión `github/gh-stack` conforme a las reglas descritas en [**AGENTS.md**](AGENTS.md).
