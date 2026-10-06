# AGENTS.md

Operational guidance for autonomous AI agents working in `RevTech-Aggregates`.

---

## 1. Project Architecture & Boundaries

- **Architecture:** Spring Boot 4.1.1 multi-module Maven reactor on Java 26 (Oracle JDK 26+), Spring Cloud 2025.1.3.
- **Trunk Branch:** `master` (not `main`).
- **Domain Contexts (DDD):** Reference specifications live in `docs/DISEÑO TÁCTICO - REVTECH.md`.
- **Submodules (Gateway + Microservices):**
  - `msvc-gateway` (`org.parangaricutirimicuaro.springcloud.msvc.gateway`) - Single Entrypoint / API Gateway (Port 8000)
  - `msvc-inspection` (`org.parangaricutirimicuaro.msvc_inspection`) - Core Bounded Context (Port 8080)
  - `msvc-clientes` (`org.parangaricutirimicuaro.msvc_clientes`) - Client & Vehicle Support Service (Port 8081)
  - `msvc-identidad` (`org.parangaricutirimicuaro.msvc_identidad`) - Identity & Access Support Service (Port 8082)
  - `msvc-administrativa` (`org.parangaricutirimicuaro.msvc_administrativa`)
  - `msvc-citas` (`org.parangaricutirimicuaro.msvc_citas`)
  - `msvc-pagos` (`org.parangaricutirimicuaro.msvc_pagos`)
- **Package Layout — `msvc-inspection` (Hexagonal / Ports & Adapters):** see `docs/ARQUITECTURA-HEXAGONAL-INSPECCION.md`.
  - `domain/model/` - Aggregate root `InspeccionTecnica`, internal entities, value objects (records), typed IDs and catalog enums. Pure Java: no Spring, JPA or Lombok.
  - `domain/event/` - Domain events (`InspeccionFinalizada`), recorded by the aggregate and published after persisting
  - `domain/exception/` - Domain rule violations
  - `application/port/in/` - Use-case interfaces (one per use case) and their command records
  - `application/port/out/` - Outbound ports (repository, vehicle, user, MTC)
  - `application/service/` - Use-case implementation; Spring-free, wired in `config/`
  - `adapter/in/web/` - REST controller, request/response DTOs, `@RestControllerAdvice`
  - `adapter/in/event/` - Spring `@EventListener`s that route domain events to use cases
  - `adapter/out/event/` - Domain event publisher (Spring `ApplicationEventPublisher`)
  - `adapter/out/persistence/` - JPA entities, Spring Data repository, domain↔JPA mapper
  - `adapter/out/client/` - Feign & Mock clients (`revtech.clients.mock`) and their port adapters
  - `config/` - Composition root (bean wiring, Feign, Scalar)
  - Dependency rule (adapters → application → domain) is enforced by `HexagonalArchitectureTest` (ArchUnit).
- **Package Layout — remaining microservices (layered):**
  - `model/entity/` - JPA Entities
  - `model/value/` - Value Objects (Embeddables / Records)
  - `model/type/` - Enums and domain type descriptors
  - `model/dto/` - DTOs for external boundaries / REST requests & responses
  - `repository/` - Spring Data JPA repositories
  - `service/` & `service/impl/` - Domain & application service layer
  - `controller/` - REST Controllers

---

## 2. Environment & Database Gotchas

- **MySQL 8.4 LTS:** Microservices expect dedicated MySQL databases and distinct credentials (defined in `docker-compose.yml` and provisioned by `docker/mysql/init-databases.sql`).
- **Live Database Dependency for Tests:** Standard Spring Boot tests (`@SpringBootTest contextLoads()`) require the live MySQL container running on port `3306`.
- **Start Local DB:**
  ```powershell
  docker compose up -d mysql
  ```
- **Environment Template:** See `.env.example` for connection variables.

---

## 3. Deterministic Developer Commands

RevTech provides unified task orchestration via `mise` (`mise.toml`) alongside standard Maven wrapper commands (`./mvnw` or `mvnw.cmd` on Windows pwsh):

### Task Orchestration (`mise`)
```powershell
# Start / stop MySQL 8.4 container
mise run db:up
mise run db:down
mise run db:logs
mise run db:recreate   # DESTRUCTIVE: wipes the MySQL volume and re-runs init-databases.sql

# Run a targeted microservice locally on host (default: msvc-inspection)
mise run dev msvc-inspection
mise run dev msvc-clientes
mise run dev msvc-identidad
mise run dev msvc-gateway

# Build OCI container images with Paketo Cloud Native Buildpacks
mise run app:build

# Full containerized stack (MySQL + all microservices + API Gateway)
mise run app:up
mise run app:down
mise run app:logs
mise run app:restart

# Lifecycle shortcuts
mise run compile
mise run test
mise run package
```

### Direct Maven Commands (`./mvnw`)
```powershell
# Compile all microservices
./mvnw clean compile

# Compile a targeted microservice (fast feedback)
./mvnw compile -pl msvc-identidad

# Package everything without executing integration tests
./mvnw package -DskipTests

# Run tests on a single service (requires docker mysql up)
./mvnw test -pl msvc-identidad

# Run single targeted test class
./mvnw test -pl msvc-identidad -Dtest=UsuarioRepositoryTest

# Run a single service locally
./mvnw spring-boot:run -pl msvc-identidad
```

---

## 4. Git Branching & Stacked PRs (`gh-stack`)

Stacked PRs are managed via the GitHub CLI extension `github/gh-stack`.

### Critical Non-Interactive Rules (Avoid TTY Freezes)
Never execute bare commands that trigger interactive TUIs or prompts. Always supply flags:

| Safe non-interactive command | Avoid bare command | Reason |
|---|---|---|
| `gh stack view --json` or `--short` | `gh stack view` | Opens interactive TUI |
| `gh stack submit --auto --open` | `gh stack submit` | Interactive title prompt |
| `gh stack merge <target> --yes --merge` | `gh pr merge` | Cannot handle stacked PR tree |
| `gh stack add <branch>` | - | Must be run from stack top (`gh stack top`) |

### Daily Stack Workflow
```powershell
# Check current stack state
gh stack view --short

# Move across layers
gh stack top
gh stack down
gh stack up

# Modify lower layer and cascade changes upward
gh stack down
# edit files and commit: git commit -m "..."
gh stack rebase --upstack
gh stack top
gh stack push

# Add next feature layer on top
gh stack add feat/nueva-capa

# Push and create/update chained PRs on GitHub
gh stack submit --auto --open

# Merge stack to master and prune local/remote merged branches
gh stack merge <target_pr_or_stack> --yes --merge
gh stack sync --prune
```

---

## 5. Commit Standards

- Follow Conventional Commits format in English:
  `feat:`, `fix:`, `refactor:`, `build:`, `docs:`, `chore:`
- Use imperative mood, lowercase subject: e.g., `feat: add msvc-clientes domain entities and crud repositories`.
