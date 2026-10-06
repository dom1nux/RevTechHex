package org.parangaricutirimicuaro.revtech.adapter.out.client;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.UsuarioResponseDto;
import org.parangaricutirimicuaro.revtech.application.inspeccion.exception.ServicioExternoNoDisponibleException;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.UsuarioPort.UsuarioInfo;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CondicionInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PersonalId;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientAdaptersTest {

    @Mock
    private UsuarioClient usuarioClient;
    @Mock
    private MtcClient mtcClient;

    private static FeignException.NotFound notFound() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/usuarios/1", Map.of(), null,
                StandardCharsets.UTF_8, null);
        return new FeignException.NotFound("Not Found", request, null, Map.of());
    }

    @Test
    @DisplayName("Una respuesta nula o un HTTP 404 significan usuario inexistente")
    void usuarioInexistente() {
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(null);
        when(usuarioClient.obtenerUsuarioPorId(2L)).thenThrow(notFound());
        UsuarioClientAdapter adapter = new UsuarioClientAdapter(usuarioClient);

        assertThat(adapter.buscarPorId(PersonalId.of(1L))).isEmpty();
        assertThat(adapter.buscarPorId(PersonalId.of(2L))).isEmpty();
    }

    @Test
    @DisplayName("Cualquier otro fallo se reporta como servicio externo no disponible")
    void usuarioServicioCaido() {
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenThrow(new IllegalStateException("Connection refused"));

        assertThatThrownBy(() -> new UsuarioClientAdapter(usuarioClient).buscarPorId(PersonalId.of(1L)))
                .isInstanceOf(ServicioExternoNoDisponibleException.class)
                .hasMessageContaining("msvc-identidad")
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
