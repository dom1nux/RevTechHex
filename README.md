# RevTech — Plataforma Integral de Inspección Técnica Vehicular

Plataforma basada en un **monolito modular con Arquitectura Hexagonal** y **Domain-Driven Design (DDD)** para la gestión y certificación técnica vehicular conforme a la normativa regulatoria.

---

## 1. Arquitectura y Dominio

Todo el sistema se despliega como una única aplicación, [`revtech-app`](revtech-app/) (puerto 8000), organizada por capas hexagonales y, dentro de cada capa, por contexto delimitado (*Bounded Context*) según el documento de referencia [**DISEÑO TÁCTICO - REVTECH.md**](docs/DISEÑO%20TÁCTICO%20-%20REVTECH.md). El detalle está en [**ARQUITECTURA-HEXAGONAL.md**](docs/ARQUITECTURA-HEXAGONAL.md).

* **Subdominio Core:**
  * `inspeccion`: ciclo de vida de la inspección técnica vehicular, registro de pruebas por sistemas (frenos, emisiones, suspensión, dirección, etc.) y emisión mutuamente excluyente del **Certificado de Inspección** (Apto) o del **Acta de Observaciones** (Observado).
* **Subdominios de Soporte / Genéricos:**
  * `clientes`: gestión de clientes y parque vehicular.
  * `identidad`: control de acceso y personal (inspectores, supervisores).
  * *Pendientes:* `citas` (agendamiento), `pagos` (cobros) y `administrativa` (líneas de inspección y catálogos). Se agregarán como nuevos contextos dentro de `revtech-app`.
* **Sistema externo:** MTC, integrado mediante OpenFeign, con un mock en memoria activo por defecto (`revtech.clients.mock=true`).

Los contextos se comunican en proceso únicamente a través de sus puertos de entrada, y ArchUnit verifica ese aislamiento en cada build.

---

## 2. Pila Tecnológica

| Componente | Tecnología |
|---|---|
| **Lenguaje** | Java 26 (Oracle JDK 26+) |
| **Framework Base** | Spring Boot 4.1.1 |
| **Integración externa** | Spring Cloud 2025.1.3 (OpenFeign, solo MTC) |
| **Persistencia** | Spring Data JPA / Hibernate |
| **Base de Datos** | MySQL 8.4 LTS (un esquema `revtech`, tablas por contexto) |
| **Arquitectura verificada** | ArchUnit |
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

RevTech cuenta con una interfaz unificada de tareas gestionada por [**mise**](https://mise.jdx.dev) (`mise.toml`).

### Modalidad A: Desarrollo Local (Host + Base de Datos en Docker)
Ideal para desarrollo activo en tu IDE con Java 26, depuración con breakpoints y reinicio rápido:

```powershell
# 1. Iniciar únicamente la base de datos MySQL 8.4 LTS
mise run db:up

# 2. Levantar la aplicación en el puerto 8000
mise run dev
```

### Modalidad B: Plataforma Completa en Contenedores
Ideal para verificar la imagen de despliegue:

```powershell
# 1. Construir la imagen OCI con Cloud Native Buildpacks
mise run app:build

# 2. Levantar la plataforma (MySQL + revtech-app)
mise run app:up

# 3. Monitorear los logs en tiempo real
mise run app:logs

# 4. Detener todo el sistema
mise run app:down
```

### Modalidad C: Sin Docker (MySQL instalado localmente)
Para ejecutar el proyecto con un MySQL instalado directamente en el equipo, sin Docker ni `mise`:

```powershell
# 1. Crear la base de datos y el usuario (una sola vez; pide la contraseña de root)
mysql -u root -p < docker/mysql/init-databases.sql

# 2. Levantar la aplicación (Windows; en Linux/macOS usar ./mvnw)
mvnw.cmd spring-boot:run -pl revtech-app
```

Por defecto, la aplicación se conecta a `localhost:3306` con el usuario `revtech_user`. Si MySQL usa otro host o puerto, defínelo antes de levantarla:

```powershell
$env:DB_HOST="localhost"; $env:DB_PORT="3307"
```

> **Volúmenes anteriores:** si tu volumen de MySQL se creó cuando el sistema eran microservicios, todavía no tiene la base `revtech`. Ejecuta `mise run db:recreate` (borra los datos) o aplica `docker/mysql/init-databases.sql` como root.

---

## 5. Referencia de Comandos Unificados (`mise`)

| Comando de `mise` | Comando Nativo Equivalente | Descripción |
|---|---|---|
| `mise run db:up` | `docker compose up -d mysql` | Inicia el contenedor MySQL 8.4 LTS |
| `mise run db:down` | `docker compose down` | Detiene el contenedor MySQL |
| `mise run db:logs` | `docker compose logs -f mysql` | Logs en vivo de MySQL |
| `mise run db:recreate` | `docker compose down -v` + `up -d mysql` | Recrea la base desde cero (**borra los datos**) |
| `mise run dev` | `./mvnw spring-boot:run -pl revtech-app` | Ejecuta la aplicación en el host |
| `mise run app:build` | `./mvnw spring-boot:build-image -DskipTests` | Construye la imagen OCI con Paketo Buildpacks |
| `mise run app:up` | `docker compose -f docker-compose.full.yml up -d` | Levanta todo el stack en Docker |
| `mise run app:down` | `docker compose -f docker-compose.full.yml down` | Detiene todo el stack |
| `mise run app:logs` | `docker compose -f docker-compose.full.yml logs -f` | Logs en vivo de todo el stack |
| `mise run app:restart` | `docker compose -f docker-compose.full.yml restart` | Reinicia todos los contenedores |
| `mise run compile` | `./mvnw clean compile` | Compila el proyecto |
| `mise run package` | `./mvnw package -DskipTests` | Empaqueta el JAR sin ejecutar pruebas |
| `mise run test` | `./mvnw test` | Ejecuta las pruebas |

---

## 6. Documentación Interactiva

Con la aplicación levantada en el puerto `8000`:
* 👉 **Portal Scalar:** [http://localhost:8000/scalar](http://localhost:8000/scalar)
* **OpenAPI completo:** [http://localhost:8000/v3/api-docs/all](http://localhost:8000/v3/api-docs/all)
* **OpenAPI por contexto:** `/v3/api-docs/inspeccion`, `/v3/api-docs/clientes`, `/v3/api-docs/identidad`
* Para una guía paso a paso de los flujos de inspección, consulta el [**README de revtech-app**](revtech-app/README.md).

---

## 7. Flujo de Trabajo y Ramas (`gh-stack`)

* **Rama principal:** `main`.
* **Commits:** Formato *Conventional Commits* en inglés (`feat:`, `fix:`, `refactor:`, `build:`, `docs:`, `chore:`).
* **Stacked PRs:** Se gestionan mediante la extensión `github/gh-stack` conforme a las reglas descritas en [**AGENTS.md**](AGENTS.md).
