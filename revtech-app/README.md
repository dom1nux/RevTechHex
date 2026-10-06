# `revtech-app`

Único desplegable de RevTech: un monolito modular hexagonal con los contextos **Inspección** (core), **Clientes** e
**Identidad**. Para entender el negocio y la arquitectura, empieza por la [ruta de lectura](../README.md#1-ruta-de-lectura).

> [!NOTE]
> Inspección valida vehículos y personal **en proceso** contra los contextos Clientes e Identidad, que usan datos reales
> en MySQL. El único sistema simulado es el **MTC** externo (`revtech.clients.mock=true` por defecto): `MockMtcClient`
> emite un código `MTC-REV-XXXXXXXX` y lo registra en consola.

---

## 1. Arranque

```powershell
# Desde la raíz del repositorio
mise run db:up
mise run dev            # o: ./mvnw spring-boot:run -pl revtech-app
```

* **API Base:** `http://localhost:8000/api`
* **Scalar API Reference:** [http://localhost:8000/scalar](http://localhost:8000/scalar)
* **OpenAPI JSON:** `http://localhost:8000/v3/api-docs/all`

---

## 2. Datos Previos

Una inspección necesita un vehículo registrado y un inspector activo.

### 2.1 Rol de acceso
Todavía no hay endpoint para administrar roles, así que se crea con SQL:

```powershell
docker exec revtech-mysql mysql -urevtech_user -prevtech_pass revtech `
  -e "INSERT INTO roles_acceso (nombre_rol, estado_activo) VALUES ('ROLE_INSPECTOR', 1);"
```

### 2.2 Inspector
```bash
curl -X POST http://localhost:8000/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{ "username": "inspector_juan", "password": "secreto123", "rolActivoId": 1 }'
```
Respuesta `201` con `idUsuario` (en adelante, `1`).

### 2.3 Vehículos
```bash
curl -X POST http://localhost:8000/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{ "clienteId": 1, "placa": "ABC-123", "categoria": "M1", "marca": "Toyota", "modelo": "Corolla", "anioFabricacion": 2020 }'

curl -X POST http://localhost:8000/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{ "clienteId": 1, "placa": "XYZ-789", "categoria": "N3", "marca": "Volvo", "modelo": "FH", "anioFabricacion": 2022 }'
```
Respuestas `201` con `idVehiculo` (en adelante, `1` y `2`).

---

## 3. Flujos de Inspección

### Flujo 1: Inspección aprobada → Certificado (`APTO`)

```bash
# Registrar (vehículo 1, inspector 1)
curl -X POST http://localhost:8000/api/inspecciones \
  -H "Content-Type: application/json" \
  -d '{ "idVehiculo": 1, "idInspector": 1, "tipoInspeccion": "INSPECCION" }'

# Iniciar (asumiendo idInspeccion = 1)
curl -X PUT http://localhost:8000/api/inspecciones/1/iniciar

# Registrar pruebas
curl -X POST http://localhost:8000/api/inspecciones/1/pruebas \
  -H "Content-Type: application/json" -d '{ "prueba": "Frenos", "resultado": "APROBADO" }'
curl -X POST http://localhost:8000/api/inspecciones/1/pruebas \
  -H "Content-Type: application/json" -d '{ "prueba": "Emisiones", "resultado": "APROBADO" }'

# Finalizar
curl -X PUT http://localhost:8000/api/inspecciones/1/finalizar \
  -H "Content-Type: application/json" \
  -d '{ "observaciones": "Vehículo en condiciones técnicas óptimas de circulación" }'
```

Resultado: `estadoProceso: "FINALIZADA"`, `resultado.condicion: "APTO"`, `certificado` no nulo y `acta: null`.
En consola aparece `[MOCK-MTC] Notificando inspección 1 para placa ABC-123 con resultado APTO`.

### Flujo 2: Inspección observada → Acta de Observaciones (`OBSERVADO`)

```bash
curl -X POST http://localhost:8000/api/inspecciones \
  -H "Content-Type: application/json" \
  -d '{ "idVehiculo": 2, "idInspector": 1, "tipoInspeccion": "INSPECCION" }'
curl -X PUT http://localhost:8000/api/inspecciones/2/iniciar
curl -X POST http://localhost:8000/api/inspecciones/2/pruebas \
  -H "Content-Type: application/json" -d '{ "prueba": "Emisiones", "resultado": "RECHAZADO" }'
curl -X PUT http://localhost:8000/api/inspecciones/2/finalizar \
  -H "Content-Type: application/json" \
  -d '{ "observaciones": "Opacidad de humos excede el límite permisible" }'
```

Resultado: `resultado.condicion: "OBSERVADO"`, `acta` no nula y `certificado: null`.

### Flujo 3: Consultar

```bash
curl http://localhost:8000/api/inspecciones/1
```

### Flujo 4: Invariantes y validaciones

| Caso | Respuesta |
|---|---|
| Vehículo o inspector inexistente (o inspector suspendido) | `422` Referencia externa inválida |
| Registrar pruebas sin iniciar la inspección | `409` |
| Finalizar sin pruebas registradas | `409` |
| Registrar un usuario con un username existente | `409` |

> Las pruebas válidas son `Frenos`, `Dirección`, `Suspensión`, `Luces`, `Neumáticos` y `Emisiones`.

---

## 4. MTC Real

Para notificar al MTC real en lugar del mock:

```properties
revtech.clients.mock=false
clients.mtc.url=http://mtc-gateway:8089
```

No se requiere ningún cambio en el dominio ni en los casos de uso.
