# 04 · Cómo agregar un contexto nuevo (ejemplo: Citas)

> **Tiempo de lectura:** 10 minutos. **Antes:** [03 · Recorrido de una petición](03-recorrido-de-una-peticion.md).

Citas, Pagos y Administrativa todavía no están implementados. Esta guía es la receta para agregar cualquiera de
ellos. El ejemplo es **Citas** (agenda y turnos de inspección) y cada paso apunta al archivo equivalente de
**Clientes**, que es el contexto más simple para copiar. `…/` significa
`revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/`.

> Antes de programar, revisa en el [DISEÑO TÁCTICO](DISEÑO%20TÁCTICO%20-%20REVTECH.md) qué entidades y objetos de valor
> define el contexto. Para Citas se propone el objeto de valor `HorarioCita`.

---

## Lista de pasos

### 1. Dominio — las reglas, en Java puro
Carpeta: `…/domain/citas/`

- [ ] `model/CitaId.java`: identificador tipado. Copia [`VehiculoId`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/domain/clientes/model/VehiculoId.java).
- [ ] `model/HorarioCita.java`: objeto de valor (`record`) que valide sus datos en el constructor.
- [ ] `model/Cita.java`: entidad o agregado con `registrar(...)` para crear y `reconstituir(...)` para recargar desde la
      base de datos. Copia [`Vehiculo`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/domain/clientes/model/Vehiculo.java).
- [ ] `exception/DatoCitaInvalidoException.java`. Copia [`DatoClienteInvalidoException`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/domain/clientes/exception/DatoClienteInvalidoException.java).

Si Citas necesita referirse a un vehículo, guarda **solo su identificador** (por ejemplo, su propio `VehiculoId`); nunca importes `domain.clientes`.

### 2. Aplicación — los casos de uso
Carpeta: `…/application/citas/`

- [ ] `port/in/ProgramarCitaUseCase.java`: interfaz con el método y su `record` de comando. Copia [`CrearVehiculoUseCase`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/application/clientes/port/in/CrearVehiculoUseCase.java).
- [ ] `port/out/CitaRepositoryPort.java`. Copia [`VehiculoRepositoryPort`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/application/clientes/port/out/VehiculoRepositoryPort.java).
- [ ] `service/CitaApplicationService.java`: implementa los casos de uso **sin anotaciones de Spring**. Copia [`VehiculoApplicationService`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/application/clientes/service/VehiculoApplicationService.java).

### 3. Configuración — registrar el servicio
- [ ] `…/config/citas/CitasConfig.java` con un `@Bean` por servicio. Copia [`ClientesConfig`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/config/clientes/ClientesConfig.java).

### 4. Persistencia — la tabla
Carpeta: `…/adapter/out/persistence/citas/`

- [ ] `entity/CitaJpaEntity.java` con `@Entity` y un nombre de tabla que no choque con los existentes (por ejemplo, `citas`). Copia [`VehiculoJpaEntity`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/out/persistence/clientes/entity/VehiculoJpaEntity.java).
- [ ] `SpringDataCitaRepository.java` (interfaz de paquete). Copia [`SpringDataVehiculoRepository`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/out/persistence/clientes/SpringDataVehiculoRepository.java).
- [ ] `CitasPersistenceMapper.java`. Copia [`ClientesPersistenceMapper`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/out/persistence/clientes/ClientesPersistenceMapper.java).
- [ ] `CitaPersistenceAdapter.java` con `@Component`, que implementa el puerto de salida. Copia [`VehiculoPersistenceAdapter`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/out/persistence/clientes/VehiculoPersistenceAdapter.java).

### 5. Web — el endpoint
Carpeta: `…/adapter/in/web/citas/`

- [ ] `dto/` con los records de request y response. Copia [`dto` de clientes](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/in/web/clientes/dto/).
- [ ] `CitaController.java` en `/api/citas`, que solo depende de puertos de entrada. Copia [`VehiculoController`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/in/web/clientes/VehiculoController.java).
- [ ] En [`GlobalExceptionHandler`](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/in/web/shared/GlobalExceptionHandler.java), agrega un `@ExceptionHandler` para cada excepción nueva.

### 6. Si Citas necesita datos de otro contexto
Ejemplo: comprobar que el vehículo existe antes de programar la cita.

- [ ] En Citas, crea un puerto de salida propio: `…/application/citas/port/out/VehiculoPort.java`, con solo los datos que necesitas.
- [ ] Impleméntalo en `…/adapter/out/integration/citas/VehiculoIntegrationAdapter.java` llamando a
      `ConsultarVehiculoUseCase` de Clientes. Copia el de [Inspección](../revtech-app/src/main/java/org/parangaricutirimicuaro/revtech/adapter/out/integration/inspeccion/VehiculoIntegrationAdapter.java).

### 7. Documentación de la API
- [ ] En [`application.properties`](../revtech-app/src/main/resources/application.properties), agrega un grupo
      `springdoc.group-configs[N]` con `paths-to-match=/api/citas/**` y una entrada `scalar.sources[N]`.

### 8. Pruebas
- [ ] Agrega `"citas"` a la lista `CONTEXTOS` de [`HexagonalArchitectureTest`](../revtech-app/src/test/java/org/parangaricutirimicuaro/revtech/architecture/HexagonalArchitectureTest.java).
- [ ] Prueba el servicio de aplicación con un repositorio en memoria. Copia [`VehiculoApplicationServiceTest`](../revtech-app/src/test/java/org/parangaricutirimicuaro/revtech/application/clientes/service/VehiculoApplicationServiceTest.java).
- [ ] Ejecuta `./mvnw test -pl revtech-app`. Si ArchUnit falla, el mensaje indica qué clase rompió qué regla.

### 9. Documentos
- [ ] Marca el contexto como implementado en [01 · El negocio](01-el-negocio.md) y agrega sus términos al glosario.
- [ ] Agrega sus carpetas, tablas y errores en [02 · Arquitectura](02-arquitectura.md).

---

## Errores frecuentes

| Síntoma | Causa | Solución |
|---|---|---|
| `NoSuchBeanDefinitionException` para tu servicio | Falta el `@Bean` en `config/citas` | Paso 3 |
| ArchUnit: *"should not depend on classes that reside in … domain.clientes"* | Importaste el modelo de otro contexto | Usa un puerto de salida y un adaptador de integración (paso 6) |
| ArchUnit: el dominio depende de `org.springframework` o `jakarta` | Pusiste anotaciones de Spring o JPA en el dominio | Muévelas a la entidad JPA o al adaptador |
| El endpoint no aparece en Scalar | Falta el grupo de springdoc | Paso 7 |
