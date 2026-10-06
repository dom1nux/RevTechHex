package org.parangaricutirimicuaro.msvc_inspection.application.service;

import org.parangaricutirimicuaro.msvc_inspection.application.exception.ReferenciaExternaInvalidaException;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.ConsultarInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.CrearInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.FinalizarInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.IniciarInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.RegistrarPruebaUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.InspeccionRepositoryPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.PublicadorEventosPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.UsuarioPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.VehiculoPort;
import org.parangaricutirimicuaro.msvc_inspection.domain.event.EventoDominio;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.InspeccionNoEncontradaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionId;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionTecnica;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.PersonalId;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.ResultadoPrueba;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.TipoInspeccion;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.VehiculoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Orquesta los casos de uso del contexto Inspección. No contiene reglas de negocio: las delega en el
 * agregado {@link InspeccionTecnica}, se comunica con el exterior a través de puertos y publica los
 * eventos de dominio una vez persistido el agregado.
 */
public class InspeccionApplicationService implements CrearInspeccionUseCase, IniciarInspeccionUseCase,
        RegistrarPruebaUseCase, FinalizarInspeccionUseCase, ConsultarInspeccionUseCase {

    private static final Logger log = LoggerFactory.getLogger(InspeccionApplicationService.class);

    private final InspeccionRepositoryPort repository;
    private final VehiculoPort vehiculoPort;
    private final UsuarioPort usuarioPort;
    private final PublicadorEventosPort publicador;
    private final Clock clock;

    public InspeccionApplicationService(InspeccionRepositoryPort repository, VehiculoPort vehiculoPort,
                                        UsuarioPort usuarioPort, PublicadorEventosPort publicador, Clock clock) {
        this.repository = repository;
        this.vehiculoPort = vehiculoPort;
        this.usuarioPort = usuarioPort;
        this.publicador = publicador;
        this.clock = clock;
    }

    @Override
    public InspeccionTecnica crear(CrearInspeccionCommand command) {
        log.info("Iniciando creación de inspección para vehículo ID: {}", command.idVehiculo());

        VehiculoId vehiculo = VehiculoId.of(command.idVehiculo());
        PersonalId inspector = PersonalId.of(command.idInspector());
        PersonalId supervisor = PersonalId.ofNullable(command.idSupervisor());
        InspeccionTecnica inspeccion = switch (TipoInspeccion.desde(command.tipoInspeccion())) {
            case INSPECCION -> {
                if (command.idInspeccionOrigen() != null) {
                    throw new DatoInvalidoException("Solo una reinspección puede referenciar una inspección de origen");
                }
                yield InspeccionTecnica.registrar(vehiculo, inspector, supervisor);
            }
            case REINSPECCION -> {
                if (command.idInspeccionOrigen() == null) {
                    throw new DatoInvalidoException("Una reinspección debe referenciar la inspección de origen");
                }
                InspeccionTecnica origen = buscarInspeccion(InspeccionId.of(command.idInspeccionOrigen()));
                yield InspeccionTecnica.reinspeccionar(origen, vehiculo, inspector, supervisor);
            }
        };

        vehiculoPort.buscarPorId(vehiculo)
                .orElseThrow(() -> new ReferenciaExternaInvalidaException(
                        "Vehículo no encontrado con ID: " + vehiculo.valor()));
        validarPersonalActivo(inspector, "Inspector");
        if (supervisor != null) {
            validarPersonalActivo(supervisor, "Supervisor");
        }
        return repository.guardar(inspeccion);
    }

    @Override
    public InspeccionTecnica iniciar(Long idInspeccion) {
        log.info("Iniciando proceso técnico de inspección ID: {}", idInspeccion);
        InspeccionTecnica inspeccion = buscarInspeccion(InspeccionId.of(idInspeccion));
        inspeccion.iniciar(ahora());
        return repository.guardar(inspeccion);
    }

    @Override
    public InspeccionTecnica registrar(RegistrarPruebaCommand command) {
        log.info("Registrando prueba '{}' con resultado '{}' en inspección ID: {}",
                command.prueba(), command.resultado(), command.idInspeccion());
        InspeccionTecnica inspeccion = buscarInspeccion(InspeccionId.of(command.idInspeccion()));
        inspeccion.registrarPrueba(ResultadoPrueba.de(command.prueba(), command.resultado()));
        return repository.guardar(inspeccion);
    }

    @Override
    public InspeccionTecnica finalizar(Long idInspeccion, String observaciones) {
        log.info("Finalizando inspección ID: {}", idInspeccion);
        InspeccionTecnica inspeccion = buscarInspeccion(InspeccionId.of(idInspeccion));
        inspeccion.finalizar(observaciones, ahora());
        List<EventoDominio> eventos = inspeccion.extraerEventos();
        InspeccionTecnica finalizada = repository.guardar(inspeccion);
        publicador.publicar(eventos);
        return finalizada;
    }

    @Override
    public InspeccionTecnica obtenerPorId(Long idInspeccion) {
        return buscarInspeccion(InspeccionId.of(idInspeccion));
    }

    private void validarPersonalActivo(PersonalId personal, String rol) {
        boolean activo = usuarioPort.buscarPorId(personal)
                .map(UsuarioPort.UsuarioInfo::activo)
                .orElse(false);
        if (!activo) {
            throw new ReferenciaExternaInvalidaException(rol + " no encontrado o inactivo con ID: " + personal.valor());
        }
    }

    private InspeccionTecnica buscarInspeccion(InspeccionId id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new InspeccionNoEncontradaException(id.valor()));
    }

    private LocalDateTime ahora() {
        return LocalDateTime.now(clock);
    }
}
