# Microservicio API Gateway (`msvc-gateway`)

El microservicio **`msvc-gateway`** actúa como el **punto de entrada único (Single Entrypoint)** para clientes web (SPAs), aplicaciones móviles y consumidores externos de la plataforma RevTech.

Construido sobre **Spring Cloud Gateway Server WebMVC** (Spring Boot 4.1.1 en Java 26), centraliza:
1. **Enrutamiento perimetral inteligente** hacia los microservicios downstream.
2. **Políticas globales de CORS** para permitir consumo seguro desde orígenes web (Vite, React, Angular, Vue).
3. **Portal interactivo unificado de documentación** mediante **Scalar API Reference** (`/scalar`).

---

## 1. Tabla de Enrutamiento

| Prefijo de Ruta | Microservicio Destino | URL por Defecto | Variable de Entorno |
|---|---|---|---|
| `/api/inspecciones/**` | `msvc-inspection` | `http://localhost:8080` | `INSPECTION_SERVICE_URL` |
| `/api/vehiculos/**` | `msvc-clientes` | `http://localhost:8081` | `CLIENTES_SERVICE_URL` |
| `/api/clientes/**` | `msvc-clientes` | `http://localhost:8081` | `CLIENTES_SERVICE_URL` |
| `/api/usuarios/**` | `msvc-identidad` | `http://localhost:8082` | `IDENTIDAD_SERVICE_URL` |
| `/api/auth/**` | `msvc-identidad` | `http://localhost:8082` | `IDENTIDAD_SERVICE_URL` |

### Enrutamiento de OpenAPI / Documentación Downstream

| Ruta en Gateway | Destino Downstream |
|---|---|
| `/v3/api-docs/inspection` | `http://localhost:8080/v3/api-docs` |
| `/v3/api-docs/clientes` | `http://localhost:8081/v3/api-docs` |
| `/v3/api-docs/identidad` | `http://localhost:8082/v3/api-docs` |

---

## 2. Portal Unificado de Documentación (Scalar)

El Gateway expone un visor Scalar unificado en:
👉 **[http://localhost:8000/scalar](http://localhost:8000/scalar)**

Desde allí se pueden inspeccionar y probar interactivamente los contratos OpenAPI de todos los microservicios del sistema.

---

## 3. Configuración 12-Factor (Variables de Entorno)

Todas las opciones son parametrizables vía variables de entorno sin recompilar:

| Variable | Valor por Defecto | Descripción |
|---|---|---|
| `SERVER_PORT` | `8000` | Puerto HTTP del API Gateway |
| `INSPECTION_SERVICE_URL` | `http://localhost:8080` | Dirección base de `msvc-inspection` |
| `CLIENTES_SERVICE_URL` | `http://localhost:8081` | Dirección base de `msvc-clientes` |
| `IDENTIDAD_SERVICE_URL` | `http://localhost:8082` | Dirección base de `msvc-identidad` |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:5173,http://localhost:4200` | Orígenes permitidos (separados por coma) |
| `SCALAR_ENABLED` | `true` | Activa/desactiva la interfaz Scalar |
| `SCALAR_PATH` | `/scalar` | Ruta base de la UI de documentación |

---

## 4. Instrucciones de Arranque

```powershell
# Windows (PowerShell)
.\mvnw.cmd spring-boot:run -pl :msvc-gateway

# Linux / macOS
./mvnw spring-boot:run -pl :msvc-gateway
```
