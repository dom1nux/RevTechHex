# 05 · Datos de prueba

> **Antes:** [02 · Arquitectura](02-arquitectura.md). Para entender qué se crea, mira [01 · El negocio](01-el-negocio.md).

RevTech no tiene un `seeder.sql` escrito a mano. Los datos de prueba se **generan llamando a la API real** (así pasan por
las mismas reglas de negocio que cualquier petición) y luego se **vuelcan** a `infra/mysql/seed-data.sql`, que es lo que
todos cargan.

## Cargar los datos (lo normal)

1. Levanta MySQL: `mise run db:up`.
2. Arranca la app una vez (`mise run dev`): Hibernate crea las tablas.
3. Con las tablas **vacías**, ejecuta `mise run db:seed`.

Si usas un MySQL local sin Docker: `mysql -u revtech_user -prevtech_pass revtech < infra/mysql/seed-data.sql`.

Todos los usuarios del personal usan la contraseña `RevTech2026!` (hoy se guarda sin cifrar: aún no hay autenticación).

## Qué contiene

| Dato | Cantidad | Detalle |
|---|---|---|
| Roles | 4 | `ROLE_ADMIN`, `ROLE_RECEPCIONISTA`, `ROLE_INSPECTOR`, `ROLE_SUPERVISOR` (los clientes no son usuarios) |
| Usuarios | 7 | `admin`, 2 recepcionistas, 3 inspectores, 1 supervisor |
| Clientes | 8 | Personas y una empresa de transportes |
| Vehículos | 12 | Categorías L, M1, M2, M3, N1, N2, N3, O2; dos quedan sin inspección |
| Inspecciones | 12 | 3 APTAS, 3 OBSERVADAS (con acta), 2 reinspecciones (una APTA, una EN_PROCESO), 2 EN_PROCESO, 2 REGISTRADAS |

## Regenerar los datos (cuando cambie el modelo)

Necesitas una base **vacía** y la app corriendo contra ella. Ojo: `mise run db:recreate` **borra el volumen**.

```powershell
mise run db:recreate       # base vacía
mise run dev               # crea el esquema; déjala corriendo
./infra/seed/seed.ps1      # llama a la API (actúa como administrador; no hay autenticación todavía)
# o, con curl (Git Bash/Linux/macOS):  ./infra/seed/seed.sh
mise run db:dump-seed      # reescribe infra/mysql/seed-data.sql
```

`seed.ps1` y `seed.sh` hacen lo mismo (mismos datos, la primera con PowerShell y la segunda con `curl`). Usan datos fijos (sin azar), por lo que el resultado es reproducible. El volcado es solo de datos
(`--no-create-info`, un `INSERT` por fila): el esquema sigue siendo de Hibernate. Revisa el diff antes de hacer commit.
