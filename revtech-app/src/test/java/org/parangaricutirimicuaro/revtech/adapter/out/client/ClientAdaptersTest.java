package org.parangaricutirimicuaro.revtech.adapter.out.client;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.UsuarioResponseDto;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.VehiculoResponseDto;
import org.parangaricutirimicuaro.revtech.application.inspeccion.exception.ServicioExternoNoDisponibleException;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.UsuarioPort.UsuarioInfo;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.VehiculoPort.VehiculoInfo;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CondicionInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PersonalId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.VehiculoId;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientAdaptersTest {

    @Mock
    private VehiculoClient vehiculoClient;
    @Mock
    private UsuarioClient usuarioClient;
    @Mock
    private MtcClient mtcClient;

    private static FeignException.NotFound notFound() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/vehiculos/1", Map.of(), null,
                StandardCharsets.UTF_8, null);
        return new FeignException.NotFound("Not Found", request, null, Map.of());
    }

    @Test
    @DisplayName("Traduce el vehículo externo a la vista mínima que necesita Inspección")
    void vehiculoEncontrado() {
        when(vehiculoClient.obtenerVehiculoPorId(1L))
                .thenReturn(new VehiculoResponseDto(1L, "ABC-123", "M1", "Toyota", "Corolla", 2020));

        assertThat(new VehiculoClientAdapter(vehiculoClient).buscarPorId(VehiculoId.of(1L)))
                .contains(new VehiculoInfo(VehiculoId.of(1L), "ABC-123"));
    }

    @Test
    @DisplayName("Una respuesta nula o un HTTP 404 significan vehículo inexistente")
    void vehiculoInexistente() {
        when(vehiculoClient.obtenerVehiculoPorId(1L)).thenReturn(null);
        when(vehiculoClient.obtenerVehiculoPorId(2L)).thenThrow(notFound());
        VehiculoClientAdapter adapter = new VehiculoClientAdapter(vehiculoClient);

        assertThat(adapter.buscarPorId(VehiculoId.of(1L))).isEmpty();
        assertThat(adapter.buscarPorId(VehiculoId.of(2L))).isEmpty();
    }

    @Test
    @DisplayName("Cualquier otro fallo se reporta como servicio externo no disponible")
    void vehiculoServicioCaido() {
        when(vehiculoClient.obtenerVehiculoPorId(1L)).thenThrow(new IllegalStateException("Connection refused"));

        assertThatThrownBy(() -> new VehiculoClientAdapter(vehiculoClient).buscarPorId(VehiculoId.of(1L)))
                .isInstanceOf(ServicioExternoNoDisponibleException.class)
                .hasMessageContaining("msvc-clientes")
                .hasMessageContaining("Connection refused");
    }

    @Test
    @DisplayName("Un usuario con estadoActivo nulo se considera inactivo")
    void usuarioEstadoNulo() {
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(new UsuarioResponseDto(1L, "inspector_1", "INSPECTOR", null));
        when(usuarioClient.obtenerUsuarioPorId(2L)).thenReturn(new UsuarioResponseDto(2L, "supervisor_2", "SUPERVISOR", true));
        UsuarioClientAdapter adapter = new UsuarioClientAdapter(usuarioClient);

        assertThat(adapter.buscarPorId(PersonalId.of(1L))).contains(new UsuarioInfo(PersonalId.of(1L), "INSPECTOR", false));
        assertThat(adapter.buscarPorId(PersonalId.of(2L))).contains(new UsuarioInfo(PersonalId.of(2L), "SUPERVISOR", true));
    }

    @Test
    @DisplayName("Reporta la condición al MTC con su nombre canónico")
    void reportaAlMtc() {
        new MtcClientAdapter(mtcClient).reportarResultado(InspeccionId.of(100L), "ABC-123", CondicionInspeccion.OBSERVADO);

        verify(mtcClient).validarNormativa(100L, "ABC-123", "OBSERVADO");
    }
}
