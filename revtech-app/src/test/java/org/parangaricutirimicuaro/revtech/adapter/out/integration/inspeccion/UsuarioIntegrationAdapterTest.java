package org.parangaricutirimicuaro.revtech.adapter.out.integration.inspeccion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.ConsultarUsuarioUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.ConsultarUsuarioUseCase.UsuarioDetalle;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.UsuarioPort.UsuarioInfo;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.UsuarioId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PersonalId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioIntegrationAdapterTest {

    @Mock
    private ConsultarUsuarioUseCase consultarUsuario;

    private static Usuario usuario(Long id, boolean activo) {
        return Usuario.reconstituir(UsuarioId.of(id), "user_" + id, "hash", RolId.of(2L), activo);
    }

    @Test
    @DisplayName("Traduce el usuario de Identidad a la vista mínima que necesita Inspección")
    void usuarioEncontrado() {
        when(consultarUsuario.buscarPorId(1L))
                .thenReturn(Optional.of(new UsuarioDetalle(usuario(1L, false), NombreRol.ROLE_INSPECTOR)));
        when(consultarUsuario.buscarPorId(2L))
                .thenReturn(Optional.of(new UsuarioDetalle(usuario(2L, true), null)));
        UsuarioIntegrationAdapter adapter = new UsuarioIntegrationAdapter(consultarUsuario);

        assertThat(adapter.buscarPorId(PersonalId.of(1L)))
                .contains(new UsuarioInfo(PersonalId.of(1L), "ROLE_INSPECTOR", false));
        assertThat(adapter.buscarPorId(PersonalId.of(2L)))
                .contains(new UsuarioInfo(PersonalId.of(2L), null, true));
    }

    @Test
    @DisplayName("Un usuario inexistente devuelve vacío")
    void usuarioInexistente() {
        when(consultarUsuario.buscarPorId(3L)).thenReturn(Optional.empty());

        assertThat(new UsuarioIntegrationAdapter(consultarUsuario).buscarPorId(PersonalId.of(3L))).isEmpty();
    }
}
