package org.parangaricutirimicuaro.msvc_inspection.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.msvc_inspection.application.exception.ReferenciaExternaInvalidaException;
import org.parangaricutirimicuaro.msvc_inspection.application.exception.ServicioExternoNoDisponibleException;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.CrearInspeccionUseCase.CrearInspeccionCommand;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.RegistrarPruebaUseCase.RegistrarPruebaCommand;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.InspeccionRepositoryPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.PublicadorEventosPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.UsuarioPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.UsuarioPort.UsuarioInfo;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.VehiculoPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.VehiculoPort.VehiculoInfo;
import org.parangaricutirimicuaro.msvc_inspection.domain.event.InspeccionFinalizada;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.InspeccionNoEncontradaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.ReglaNegocioVioladaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.TransicionEstadoInvalidaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionFixtures.*;

@ExtendWith(MockitoExtension.class)
class InspeccionApplicationServiceTest {

    private static final Instant AHORA = Instant.parse("2026-10-05T10:00:00Z");
    private static final LocalDateTime AHORA_LOCAL = LocalDateTime.ofInstant(AHORA, ZoneOffset.UTC);

    @Mock
    private InspeccionRepositoryPort repository;
    @Mock
    private VehiculoPort vehiculoPort;
    @Mock
    private UsuarioPort usuarioPort;
    @Mock
    private PublicadorEventosPort publicador;

    private InspeccionApplicationService service;

    @BeforeEach
    void setUp() {
        service = new InspeccionApplicationService(repository, vehiculoPort, usuarioPort, publicador,
                Clock.fixed(AHORA, ZoneOffset.UTC));
    }

    private void guardarDevuelveElMismo() {
        when(repository.guardar(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void personalYVehiculoValidos() {
        when(vehiculoPort.buscarPorId(VEHICULO)).thenReturn(Optional.of(new VehiculoInfo(VEHICULO, "ABC-123")));
        when(usuarioPort.buscarPorId(INSPECTOR)).thenReturn(Optional.of(new UsuarioInfo(INSPECTOR, "INSPECTOR", true)));
    }

    @Nested
    @DisplayName("Crear inspección")
    class Crear {

        private final CrearInspeccionCommand command = new CrearInspeccionCommand(1L, 1L, 2L, "inspeccion", null);

        @Test
        @DisplayName("Valida vehículo, inspector y supervisor y persiste la inspección REGISTRADA")
        void creaInspeccion() {
            personalYVehiculoValidos();
            when(usuarioPort.buscarPorId(SUPERVISOR)).thenReturn(Optional.of(new UsuarioInfo(SUPERVISOR, "SUPERVISOR", true)));
            guardarDevuelveElMismo();

            InspeccionTecnica creada = service.crear(command);

            ArgumentCaptor<InspeccionTecnica> captor = ArgumentCaptor.forClass(InspeccionTecnica.class);
            verify(repository).guardar(captor.capture());
            assertThat(creada).isSameAs(captor.getValue());
            assertThat(creada.getEstado()).isEqualTo(EstadoProceso.REGISTRADA);
            assertThat(creada.getTipo()).isEqualTo(TipoInspeccion.INSPECCION);
            assertThat(creada.getVehiculo()).isEqualTo(VEHICULO);
            assertThat(creada.getSupervisor()).isEqualTo(SUPERVISOR);
        }

        @Test
        @DisplayName("Sin supervisor no consulta a Identidad por él")
        void creaSinSupervisor() {
            personalYVehiculoValidos();
            guardarDevuelveElMismo();

            service.crear(new CrearInspeccionCommand(1L, 1L, null, null, null));

            verify(usuarioPort, times(1)).buscarPorId(any());
        }

        @Test
        @DisplayName("Falla si el vehículo no existe y no persiste nada")
        void vehiculoInexistente() {
            when(vehiculoPort.buscarPorId(VEHICULO)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.crear(command))
                    .isInstanceOf(ReferenciaExternaInvalidaException.class)
                    .hasMessageContaining("Vehículo");
            verify(repository, never()).guardar(any());
        }

        @Test
        @DisplayName("Falla si el inspector está inactivo")
        void inspectorInactivo() {
            when(vehiculoPort.buscarPorId(VEHICULO)).thenReturn(Optional.of(new VehiculoInfo(VEHICULO, "ABC-123")));
            when(usuarioPort.buscarPorId(INSPECTOR)).thenReturn(Optional.of(new UsuarioInfo(INSPECTOR, "INSPECTOR", false)));

            assertThatThrownBy(() -> service.crear(command))
                    .isInstanceOf(ReferenciaExternaInvalidaException.class)
                    .hasMessageContaining("Inspector");
            verify(repository, never()).guardar(any());
        }

        @Test
        @DisplayName("Falla si el supervisor no existe")
        void supervisorInexistente() {
            personalYVehiculoValidos();
            when(usuarioPort.buscarPorId(SUPERVISOR)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.crear(command))
                    .isInstanceOf(ReferenciaExternaInvalidaException.class)
                    .hasMessageContaining("Supervisor");
        }

        @Test
        @DisplayName("Datos inválidos se rechazan antes de consultar servicios externos")
        void datosInvalidosSinLlamadasExternas() {
            assertThatThrownBy(() -> service.crear(new CrearInspeccionCommand(1L, 1L, null, "DESCONOCIDO", null)))
                    .isInstanceOf(DatoInvalidoException.class);
            assertThatThrownBy(() -> service.crear(new CrearInspeccionCommand(null, 1L, null, null, null)))
                    .isInstanceOf(DatoInvalidoException.class);
            assertThatThrownBy(() -> service.crear(new CrearInspeccionCommand(1L, 1L, null, "INSPECCION", 50L)))
                    .isInstanceOf(DatoInvalidoException.class);
            verifyNoInteractions(vehiculoPort, usuarioPort, repository);
        }

        @Test
        @DisplayName("Un fallo del servicio externo se propaga sin persistir")
        void servicioExternoCaido() {
            when(vehiculoPort.buscarPorId(VEHICULO)).thenThrow(new ServicioExternoNoDisponibleException("caído", null));

            assertThatThrownBy(() -> service.crear(command)).isInstanceOf(ServicioExternoNoDisponibleException.class);
            verify(repository, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("Crear reinspección")
    class CrearReinspeccion {

        @Test
        @DisplayName("Delega en el agregado de origen y persiste la reinspección")
        void creaReinspeccion() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(finalizadaObservada()));
            personalYVehiculoValidos();
            guardarDevuelveElMismo();

            InspeccionTecnica reinspeccion = service.crear(new CrearInspeccionCommand(1L, 1L, null, "REINSPECCION", 100L));

            assertThat(reinspeccion.getTipo()).isEqualTo(TipoInspeccion.REINSPECCION);
            assertThat(reinspeccion.getOrigen()).isEqualTo(ID);
        }

        @Test
        @DisplayName("Exige indicar la inspección de origen")
        void sinOrigen() {
            assertThatThrownBy(() -> service.crear(new CrearInspeccionCommand(1L, 1L, null, "REINSPECCION", null)))
                    .isInstanceOf(DatoInvalidoException.class);
            verifyNoInteractions(repository);
        }

        @Test
        @DisplayName("Falla si la inspección de origen no existe")
        void origenInexistente() {
            when(repository.buscarPorId(InspeccionId.of(50L))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.crear(new CrearInspeccionCommand(1L, 1L, null, "REINSPECCION", 50L)))
                    .isInstanceOf(InspeccionNoEncontradaException.class);
            verify(repository, never()).guardar(any());
        }

        @Test
        @DisplayName("Las reglas del agregado se aplican antes de consultar otros contextos")
        void origenApto() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(finalizadaApta()));

            assertThatThrownBy(() -> service.crear(new CrearInspeccionCommand(1L, 1L, null, "REINSPECCION", 100L)))
                    .isInstanceOf(ReglaNegocioVioladaException.class);
            verifyNoInteractions(vehiculoPort, usuarioPort);
            verify(repository, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("Iniciar y registrar pruebas")
    class IniciarYRegistrar {

        @Test
        @DisplayName("Iniciar usa el reloj inyectado como fecha de inicio")
        void iniciaConReloj() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(registrada()));
            guardarDevuelveElMismo();

            InspeccionTecnica iniciada = service.iniciar(100L);

            assertThat(iniciada.getEstado()).isEqualTo(EstadoProceso.EN_PROCESO);
            assertThat(iniciada.getPeriodo().fechaInicio()).isEqualTo(AHORA_LOCAL);
        }

        @Test
        @DisplayName("Registrar prueba interpreta el catálogo y persiste")
        void registraPrueba() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(enProceso()));
            guardarDevuelveElMismo();

            InspeccionTecnica actualizada = service.registrar(new RegistrarPruebaCommand(100L, "Frenos", "aprobado"));

            assertThat(actualizada.getEvaluaciones())
                    .containsExactly(prueba(TipoPrueba.FRENOS, VeredictoPrueba.APROBADO));
            verify(repository).guardar(actualizada);
        }

        @Test
        @DisplayName("Una regla de dominio violada no persiste cambios")
        void reglaVioladaNoPersiste() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(registrada()));

            assertThatThrownBy(() -> service.registrar(new RegistrarPruebaCommand(100L, "Frenos", "APROBADO")))
                    .isInstanceOf(TransicionEstadoInvalidaException.class);
            verify(repository, never()).guardar(any());
        }

        @Test
        @DisplayName("Operar sobre una inspección inexistente lanza InspeccionNoEncontradaException")
        void inexistente() {
            when(repository.buscarPorId(InspeccionId.of(404L))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.iniciar(404L))
                    .isInstanceOf(InspeccionNoEncontradaException.class)
                    .hasMessageContaining("404");
        }
    }

    @Nested
    @DisplayName("Finalizar inspección")
    class Finalizar {

        @Test
        @DisplayName("Persiste y luego publica InspeccionFinalizada")
        void publicaEventoTrasPersistir() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(
                    enProceso(prueba(TipoPrueba.FRENOS, VeredictoPrueba.APROBADO))));
            guardarDevuelveElMismo();

            InspeccionTecnica finalizada = service.finalizar(100L, "OK");

            assertThat(finalizada.getEstado()).isEqualTo(EstadoProceso.FINALIZADA);
            assertThat(finalizada.getCertificado()).isNotNull();
            var orden = inOrder(repository, publicador);
            orden.verify(repository).guardar(any());
            orden.verify(publicador).publicar(List.of(
                    new InspeccionFinalizada(ID, VEHICULO, CondicionInspeccion.APTO, AHORA_LOCAL)));
        }

        @Test
        @DisplayName("Si la persistencia falla, no publica eventos")
        void noPublicaSiFallaPersistencia() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(
                    enProceso(prueba(TipoPrueba.FRENOS, VeredictoPrueba.APROBADO))));
            when(repository.guardar(any())).thenThrow(new IllegalStateException("BD caída"));

            assertThatThrownBy(() -> service.finalizar(100L, null)).isInstanceOf(IllegalStateException.class);
            verifyNoInteractions(publicador);
        }

        @Test
        @DisplayName("Sin pruebas registradas no finaliza, no persiste y no publica")
        void sinPruebas() {
            when(repository.buscarPorId(ID)).thenReturn(Optional.of(enProceso()));

            assertThatThrownBy(() -> service.finalizar(100L, null))
                    .isInstanceOf(TransicionEstadoInvalidaException.class);
            verify(repository, never()).guardar(any());
            verifyNoInteractions(publicador);
        }
    }

    @Test
    @DisplayName("Consultar devuelve la inspección almacenada")
    void consulta() {
        InspeccionTecnica almacenada = registrada();
        when(repository.buscarPorId(ID)).thenReturn(Optional.of(almacenada));

        assertThat(service.obtenerPorId(100L)).isSameAs(almacenada);
    }
}
