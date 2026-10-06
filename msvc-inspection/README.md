# Microservicio de Inspección (`msvc-inspection`)

El microservicio **`msvc-inspection`** es el **Subdominio Core** de RevTech. Encapsula las reglas técnicas vehiculares, la consistencia del agregado [`InspeccionTecnica`](src/main/java/org/parangaricutirimicuaro/msvc_inspection/domain/model/InspeccionTecnica.java) y la emisión reglamentaria de certificados o actas.

> [!TIP]
> **Modo Autónomo (Mocks en Memoria):**  
> Este microservicio está diseñado para **arrancar, ejecutarse y ser probado al 100% sin necesidad de que los demás microservicios (`msvc-clientes`, `msvc-identidad`, `MTC`) estén implementados o levantados**.

---

## 1. ¿Cómo Funciona la Simulación en Memoria?

El microservicio utiliza el patrón **Puertos y Adaptadores**. Cuando la propiedad `revtech.clients.mock=true` está activa (activada por defecto en [`application.properties`](src/main/resources/application.properties)), Spring Boot inyecta adaptadores simulados en memoria:

| Cliente Simulado | Comportamiento del Mock | Casos de Prueba Disponibles |
|---|---|---|
| **`MockVehiculoClient`** | Simula el catálogo de vehículos de `msvc-clientes` | • `idVehiculo: 1` $\rightarrow$ Toyota Corolla (Categoría particular `M1`, Placa `ABC-123`)<br>• `idVehiculo: 2` $\rightarrow$ Camión Volvo FH (Categoría pesada `N3`, Placa `XYZ-789`)<br>• `idVehiculo: 999` $\rightarrow$ Simula vehículo inexistente (HTTP 422) |
| **`MockUsuarioClient`** | Simula la autenticación y roles de `msvc-identidad` | • IDs impares (ej. `1`, `3`) $\rightarrow$ Rol `INSPECTOR` habilitado<br>• IDs pares (ej. `2`, `4`) $\rightarrow$ Rol `SUPERVISOR` habilitado<br>• `idUsuario: 999` $\rightarrow$ Simula usuario inexistente (HTTP 422) |
| **`MockMtcClient`** | Simula el ente regulador externo (MTC) | • Emite automáticamente código oficial de validación: `MTC-REV-XXXXXXXX` |

---

## 2. Requisitos Previos para Levantar el Servicio

Únicamente se necesita tener en ejecución la base de datos MySQL 8.4 (o cualquier 8.0.16+):

```powershell
# Desde la raíz del repositorio (RevTech-Aggregates):
docker compose up -d mysql
```

---

## 3. Instrucciones de Arranque

Ejecuta el microservicio con el Maven Wrapper:

```powershell
# Windows (PowerShell)
./mvnw.cmd spring-boot:run -pl msvc-inspection

# Linux / macOS
./mvnw spring-boot:run -pl msvc-inspection
```

Una vez levantado, la aplicación responderá en:
* **API Base:** `http://localhost:8080/api/inspecciones`
* **Scalar API Reference (Documentación interactiva):** [http://localhost:8080/scalar](http://localhost:8080/scalar)
* **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

---

## 4. Guía de Pruebas Paso a Paso (cURL / Postman / Scalar)

A continuación se presentan los flujos funcionales completos que puedes ejecutar de forma inmediata.

### Flujo 1: Inspección Aprobada $\rightarrow$ Emisión de Certificado de Inspección (`APTO`)

Este flujo simula un vehículo que aprueba todas las revisiones mecánicas y ambientales.

#### Paso 1.1: Registrar la Inspección
Valida el vehículo con ID `1` y al inspector con ID `1`.

```bash
curl -X POST http://localhost:8080/api/inspecciones \
  -H "Content-Type: application/json" \
  -d '{
    "idVehiculo": 1,
    "idInspector": 1,
    "idSupervisor": 2,
    "tipoInspeccion": "INSPECCION"
  }'
```
* **Respuesta esperada (HTTP 201 Created):**
  * `idInspeccion`: `1` (o ID generado).
  * `estadoProceso`: `"REGISTRADA"`.

---

#### Paso 1.2: Iniciar la Evaluación en Línea
Marca el inicio temporal de la revisión técnica.

```bash
curl -X PUT http://localhost:8080/api/inspecciones/1/iniciar
```
* **Respuesta esperada (HTTP 200 OK):**
  * `estadoProceso`: `"EN_PROCESO"`.
  * `periodoInspeccion.fechaInicio`: Marca de tiempo actual.

---

#### Paso 1.3: Registrar Pruebas Técnicas Aprobadas
Registra las evaluaciones individuales de los sistemas vehiculares.

```bash
# Prueba 1: Frenos
curl -X POST http://localhost:8080/api/inspecciones/1/pruebas \
  -H "Content-Type: application/json" \
  -d '{
    "prueba": "Frenos",
    "resultado": "APROBADO"
  }'

# Prueba 2: Emisiones
curl -X POST http://localhost:8080/api/inspecciones/1/pruebas \
  -H "Content-Type: application/json" \
  -d '{
    "prueba": "Emisiones",
    "resultado": "APROBADO"
  }'

# Prueba 3: Suspensión y Dirección
curl -X POST http://localhost:8080/api/inspecciones/1/pruebas \
  -H "Content-Type: application/json" \
  -d '{
    "prueba": "Suspensión",
    "resultado": "APROBADO"
  }'
```

---

#### Paso 1.4: Finalizar Inspección y Emitir Documento Oficial
El agregado evalúa que todas las pruebas son aprobatorias y **emite automáticamente el Certificado de Inspección**:

```bash
curl -X PUT http://localhost:8080/api/inspecciones/1/finalizar \
  -H "Content-Type: application/json" \
  -d '{
    "observaciones": "Vehículo en condiciones técnicas óptimas de circulación"
  }'
```

* **Resultado en la respuesta (HTTP 200 OK):**
  * `estadoProceso`: `"FINALIZADA"`
  * `resultado.condicion`: `"APTO"`
  * `certificado`: Objeto no nulo (`idCertificado`, `fechaEmision`)
  * `acta`: `null`
  * En consola: Log `[MOCK-MTC] Notificando inspección 1 para placa ABC-123 con resultado APTO`

---

### Flujo 2: Inspección Observada $\rightarrow$ Emisión de Acta de Observaciones (`OBSERVADO`)

Este flujo simula un vehículo que presenta fallas técnicas graves o desfavorables.

#### Paso 2.1: Registrar e Iniciar Nueva Inspección
```bash
# Registrar para vehículo ID 2 (Camión Volvo FH)
curl -X POST http://localhost:8080/api/inspecciones \
  -H "Content-Type: application/json" \
  -d '{
    "idVehiculo": 2,
    "idInspector": 1,
    "tipoInspeccion": "INSPECCION"
  }'

# Iniciar inspección (asumiendo idInspeccion = 2)
curl -X PUT http://localhost:8080/api/inspecciones/2/iniciar
```

---

#### Paso 2.2: Registrar Pruebas Técnicas con Deficiencias
```bash
# Prueba 1: Frenos Aprobados
curl -X POST http://localhost:8080/api/inspecciones/2/pruebas \
  -H "Content-Type: application/json" \
  -d '{
    "prueba": "Frenos",
    "resultado": "APROBADO"
  }'

# Prueba 2: Emisiones Rechazadas
curl -X POST http://localhost:8080/api/inspecciones/2/pruebas \
  -H "Content-Type: application/json" \
  -d '{
    "prueba": "Emisiones",
    "resultado": "RECHAZADO"
  }'
```

---

#### Paso 2.3: Finalizar Inspección
Al detectar la prueba rechazada, el aggregate root **emite exclusivamente el Acta de Observaciones**:

```bash
curl -X PUT http://localhost:8080/api/inspecciones/2/finalizar \
  -H "Content-Type: application/json" \
  -d '{
    "observaciones": "Opacidad de humos excede límite permisible según normativa D.S. 025-2008-MTC"
  }'
```

* **Resultado en la respuesta (HTTP 200 OK):**
  * `estadoProceso`: `"FINALIZADA"`
  * `resultado.condicion`: `"OBSERVADO"`
  * `acta`: Objeto no nulo con el detalle de las observaciones
  * `certificado`: `null`

---

### Flujo 3: Consultar Inspección por ID

Permite auditar el estado consolidado, incluyendo las pruebas ejecutadas y el documento generado:

```bash
curl -X GET http://localhost:8080/api/inspecciones/1
```

---

### Flujo 4: Prueba de Invariantes y Validaciones

1. **Vehículo inexistente en el catálogo:**
   ```bash
   curl -X POST http://localhost:8080/api/inspecciones \
     -H "Content-Type: application/json" \
     -d '{ "idVehiculo": 999, "idInspector": 1 }'
   ```
   *Respuesta:* HTTP 422 `Vehículo no encontrado con ID: 999`.

2. **Intentar registrar pruebas sin haber iniciado la inspección:**
   *Respuesta:* HTTP 409 `Solo se pueden registrar pruebas cuando la inspección está EN_PROCESO`.

3. **Intentar finalizar sin pruebas registradas:**
   *Respuesta:* HTTP 409 `No se puede finalizar la inspección sin haber registrado pruebas técnicas`.

---

## 5. Transición a Producción / Microservicios Reales

Cuando los microservicios colaboradores (`msvc-clientes`, `msvc-identidad`, `MTC`) estén implementados y desplegados, basta con modificar una línea en [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
# Desactivar adaptadores Mock en memoria
revtech.clients.mock=false

# Configurar las URLs reales de los servicios
clients.msvc-clientes.url=http://msvc-clientes:8081
clients.msvc-identidad.url=http://msvc-identidad:8082
clients.mtc.url=http://mtc-gateway:8089
```

No se requiere ningún cambio de código en la lógica de dominio ni en los servicios del microservicio.

> Las pruebas válidas son `Frenos`, `Dirección`, `Suspensión`, `Luces`, `Neumáticos` y `Emisiones`. Arquitectura y reglas: [ARQUITECTURA-HEXAGONAL-INSPECCION](../docs/ARQUITECTURA-HEXAGONAL-INSPECCION.md).
