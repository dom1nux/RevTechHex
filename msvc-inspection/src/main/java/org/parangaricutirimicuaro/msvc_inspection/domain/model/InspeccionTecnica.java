package org.parangaricutirimicuaro.msvc_inspection.domain.model;

import org.parangaricutirimicuaro.msvc_inspection.domain.event.EventoDominio;
import org.parangaricutirimicuaro.msvc_inspection.domain.event.InspeccionFinalizada;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.ReglaNegocioVioladaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.TransicionEstadoInvalidaException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Raíz del Agregado de Inspección (Core).
 * <p>
 * Encapsula el ciclo de vida de la evaluación técnica de un vehículo y es la única responsable de
 * decidir, al finalizar, si se emite un {@link CertificadoInspeccion} (APTO) o un
 * {@link ActaObservaciones} (OBSERVADO). Ningún documento puede emitirse fuera de la raíz.
 * Los hechos relevantes del negocio se registran como {@link EventoDominio} para publicarse tras persistir.
 */
public final class InspeccionTecnica {

    static final String OBSERVACION_POR_DEFECTO = "Se identificaron deficiencias técnicas en el vehículo.";

    private final InspeccionId id;
    private final VehiculoId vehiculo;
    private final PersonalId inspector;
    private final PersonalId supervisor;
    private final TipoInspeccion tipo;
    private final InspeccionId origen;
    private final List<ResultadoPrueba> evaluaciones;
    private final List<EventoDominio> eventos = new ArrayList<>();
    private EstadoProceso estado;
    private String observaciones;
    private PeriodoInspeccion periodo;
    private ResultadoInspeccion resultado;
    private CertificadoInspeccion certificado;
    private ActaObservaciones acta;

    private InspeccionTecnica(Snapshot s) {
        this.id = s.id();
        this.vehiculo = s.vehiculo();
        this.inspector = s.inspector();
        this.supervisor = s.supervisor();
        this.tipo = s.tipo();
        this.origen = s.origen();
        this.estado = s.estado();
        this.observaciones = s.observaciones();
        this.periodo = s.periodo();
        this.resultado = s.resultado();
        this.evaluaciones = new ArrayList<>(s.evaluaciones());
        this.certificado = s.certificado();
        this.acta = s.acta();
    }

    /**
     * Registra una inspección ordinaria en estado {@link EstadoProceso#REGISTRADA}.
     */
    public static InspeccionTecnica registrar(VehiculoId vehiculo, PersonalId inspector, PersonalId supervisor) {
        return new InspeccionTecnica(Snapshot.nueva(vehiculo, inspector, supervisor, TipoInspeccion.INSPECCION, null));
    }

    /**
     * Registra la reinspección de una inspección anterior. Solo procede si el origen terminó OBSERVADO
     * (es decir, emitió un acta con deficiencias a corregir) y si se trata del mismo vehículo.
     */
    public static InspeccionTecnica reinspeccionar(InspeccionTecnica origen, VehiculoId vehiculo,
                                                   PersonalId inspector, PersonalId supervisor) {
        if (origen == null || origen.id == null) {
            throw new DatoInvalidoException("Una reinspección debe referenciar una inspección de origen registrada");
        }
        if (origen.estado != EstadoProceso.FINALIZADA || origen.resultado.esApto()) {
            throw new ReglaNegocioVioladaException("Solo se puede reinspeccionar una inspección FINALIZADA con resultado "
                    + "OBSERVADO (inspección " + origen.id.valor() + ": " + origen.estado
                    + (origen.resultado != null ? ", " + origen.resultado.condicion() : "") + ")");
        }
        if (!origen.vehiculo.equals(vehiculo)) {
            throw new ReglaNegocioVioladaException("La reinspección debe realizarse al mismo vehículo de la inspección "
                    + "de origen (esperado " + origen.vehiculo.valor() + ", recibido " + (vehiculo != null ? vehiculo.valor() : null) + ")");
        }
        return new InspeccionTecnica(Snapshot.nueva(vehiculo, inspector, supervisor, TipoInspeccion.REINSPECCION, origen.id));
    }

    /**
     * Reconstruye el agregado desde su representación persistida. El snapshot valida que el estado sea
     * coherente con el ciclo de vida, por lo que no es posible reconstruir una inspección inconsistente.
     */
    public static InspeccionTecnica reconstituir(Snapshot snapshot) {
        if (snapshot.id() == null) {
            throw new IllegalArgumentException("Solo se puede reconstituir una inspección ya persistida");
        }
        return new InspeccionTecnica(snapshot);
    }

    public void iniciar(LocalDateTime ahora) {
        exigirEstado(EstadoProceso.REGISTRADA, "Solo se puede iniciar una inspección REGISTRADA");
        this.estado = EstadoProceso.EN_PROCESO;
        this.periodo = PeriodoInspeccion.iniciadoEn(ahora);
    }

    public void registrarPrueba(ResultadoPrueba prueba) {
        exigirEstado(EstadoProceso.EN_PROCESO, "Solo se pueden registrar pruebas cuando la inspección está EN_PROCESO");
        if (prueba == null) {
            throw new DatoInvalidoException("El resultado de la prueba es obligatorio");
        }
        if (evaluaciones.stream().anyMatch(e -> e.prueba() == prueba.prueba())) {
            throw new ReglaNegocioVioladaException("La prueba " + prueba.prueba() + " ya fue registrada en esta inspección");
        }
        this.evaluaciones.add(prueba);
    }

    /**
     * Evalúa las pruebas registradas y cierra la inspección emitiendo exactamente un documento:
     * certificado si ninguna prueba es deficiente; acta de observaciones en caso contrario.
     * Registra el evento {@link InspeccionFinalizada}.
     */
    public void finalizar(String observacionesFinales, LocalDateTime ahora) {
        exigirEstado(EstadoProceso.EN_PROCESO, "Solo se puede finalizar una inspección EN_PROCESO");
        if (evaluaciones.isEmpty()) {
            throw new TransicionEstadoInvalidaException(
                    "No se puede finalizar la inspección sin haber registrado pruebas técnicas");
        }

        if (evaluaciones.stream().anyMatch(ResultadoPrueba::esDeficiente)) {
            String detalle = observacionesFinales != null ? observacionesFinales : OBSERVACION_POR_DEFECTO;
            this.acta = ActaObservaciones.emitir(id, detalle, ahora);
            this.resultado = ResultadoInspeccion.OBSERVADO;
        } else {
            this.certificado = CertificadoInspeccion.emitir(id, ahora);
            this.resultado = ResultadoInspeccion.APTO;
        }

        this.observaciones = observacionesFinales;
        this.estado = EstadoProceso.FINALIZADA;
        this.periodo = periodo.finalizadoEn(ahora);
        this.eventos.add(new InspeccionFinalizada(id, vehiculo, resultado.condicion(), ahora));
    }

    /**
     * Entrega los eventos registrados desde la última extracción y los descarta del agregado.
     */
    public List<EventoDominio> extraerEventos() {
        List<EventoDominio> pendientes = List.copyOf(eventos);
        eventos.clear();
        return pendientes;
    }

    public Snapshot snapshot() {
        return new Snapshot(id, vehiculo, inspector, supervisor, tipo, origen, estado, observaciones,
                periodo, resultado, List.copyOf(evaluaciones), certificado, acta);
    }

    private void exigirEstado(EstadoProceso esperado, String mensaje) {
        if (estado != esperado) {
            throw new TransicionEstadoInvalidaException(mensaje + " (estado actual: " + estado + ")");
        }
    }

    public InspeccionId getId() {
        return id;
    }

    public VehiculoId getVehiculo() {
        return vehiculo;
    }

    public PersonalId getInspector() {
        return inspector;
    }

    public PersonalId getSupervisor() {
        return supervisor;
    }

    public TipoInspeccion getTipo() {
        return tipo;
    }

    public InspeccionId getOrigen() {
        return origen;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public PeriodoInspeccion getPeriodo() {
        return periodo;
    }

    public ResultadoInspeccion getResultado() {
        return resultado;
    }

    public List<ResultadoPrueba> getEvaluaciones() {
        return List.copyOf(evaluaciones);
    }

    public CertificadoInspeccion getCertificado() {
        return certificado;
    }

    public ActaObservaciones getActa() {
        return acta;
    }

    /**
     * Estado completo del agregado, usado para persistirlo y reconstituirlo. Su construcción verifica que
     * los datos sean coherentes con el ciclo de vida REGISTRADA → EN_PROCESO → FINALIZADA.
     */
    public record Snapshot(
            InspeccionId id,
            VehiculoId vehiculo,
            PersonalId inspector,
            PersonalId supervisor,
            TipoInspeccion tipo,
            InspeccionId origen,
            EstadoProceso estado,
            String observaciones,
            PeriodoInspeccion periodo,
            ResultadoInspeccion resultado,
            List<ResultadoPrueba> evaluaciones,
            CertificadoInspeccion certificado,
            ActaObservaciones acta
    ) {

        public Snapshot {
            if (vehiculo == null) {
                throw new DatoInvalidoException("El vehículo es obligatorio");
            }
            if (inspector == null) {
                throw new DatoInvalidoException("El inspector es obligatorio");
            }
            if (tipo == null || estado == null) {
                throw new DatoInvalidoException("El tipo y el estado de la inspección son obligatorios");
            }
            if ((tipo == TipoInspeccion.REINSPECCION) != (origen != null)) {
                throw new DatoInvalidoException("Solo una reinspección referencia una inspección de origen, y siempre debe hacerlo");
            }
            evaluaciones = evaluaciones != null ? List.copyOf(evaluaciones) : List.of();
            boolean sinDocumentos = resultado == null && certificado == null && acta == null;
            switch (estado) {
                case REGISTRADA -> exigir(periodo == null && evaluaciones.isEmpty() && sinDocumentos,
                        "una inspección REGISTRADA no tiene periodo, pruebas ni documentos");
                case EN_PROCESO -> exigir(periodo != null && periodo.fechaFin() == null && sinDocumentos,
                        "una inspección EN_PROCESO tiene periodo abierto y ningún documento");
                case FINALIZADA -> exigir(periodo != null && periodo.fechaFin() != null && !evaluaciones.isEmpty()
                                && resultado != null
                                && (resultado.esApto() ? certificado != null && acta == null : acta != null && certificado == null),
                        "una inspección FINALIZADA tiene periodo cerrado, pruebas, resultado y el documento que le corresponde");
            }
        }

        static Snapshot nueva(VehiculoId vehiculo, PersonalId inspector, PersonalId supervisor,
                              TipoInspeccion tipo, InspeccionId origen) {
            return new Snapshot(null, vehiculo, inspector, supervisor, tipo, origen, EstadoProceso.REGISTRADA,
                    null, null, null, List.of(), null, null);
        }

        private static void exigir(boolean condicion, String regla) {
            if (!condicion) {
                throw new IllegalStateException("Estado de inspección inconsistente: " + regla);
            }
        }
    }
}
