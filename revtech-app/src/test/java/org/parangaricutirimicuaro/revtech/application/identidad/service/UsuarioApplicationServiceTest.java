package org.parangaricutirimicuaro.revtech.application.identidad.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarUsuarioUseCase.RegistrarUsuarioCommand;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.RolAccesoRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.UsuarioRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.RecursoIdentidadNoEncontradoException;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.ReglaIdentidadVioladaException;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioApplicationServiceTest {

    private static final RolId INSPECTOR = RolId.of(2L);
    private static final RolId INACTIVO = RolId.of(3L);

    private final UsuariosEnMemoria usuarios = new UsuariosEnMemoria();
    private final RolAccesoRepositoryPort roles = id -> Optional.ofNullable(Map.of(
            INSPECTOR, RolAcceso.reconstituir(INSPECTOR, NombreRol.ROLE_INSPECTOR, List.of("REGISTRAR_PRUEBA"), true),
            INACTIVO, RolAcceso.reconstituir(INACTIVO, NombreRol.ROLE_MECANICO, List.of(), false)).get(id));
    private final UsuarioApplicationService service = new UsuarioApplicationService(usuarios, roles);

    private Usuario registrar(String username, Long rol) {
        return service.registrar(new RegistrarUsuarioCommand(username, "secreto123", rol));
    }

    @Test
    @DisplayName("Busca un usuario existente y resuelve el nombre de su rol")
    void buscarConRol() {
        Usuario creado = registrar("inspector_juan", 2L);

        var detalle = service.buscarPorId(creado.getId().valor()).orElseThrow();

        assertThat(detalle.usuario().getUsername()).isEqualTo("inspector_juan");
        assertThat(detalle.nombreRol()).isEqualTo(NombreRol.ROLE_INSPECTOR);
        assertThat(detalle.usuario().isEstadoActivo()).isTrue();
    }

    @Test
    @DisplayName("Un usuario inexistente devuelve vacío")
    void buscarInexistente() {
        assertThat(service.buscarPorId(999L)).isEmpty();
    }

    @Test
    @DisplayName("Rechaza un username duplicado, un rol inexistente o un rol inactivo")
    void registroInvalido() {
        registrar("ana", 2L);

        assertThatThrownBy(() -> registrar("ana", 2L)).isInstanceOf(ReglaIdentidadVioladaException.class);
        assertThatThrownBy(() -> registrar("beto", 99L)).isInstanceOf(RecursoIdentidadNoEncontradoException.class);
        assertThatThrownBy(() -> registrar("carla", 3L)).isInstanceOf(ReglaIdentidadVioladaException.class);
    }

    private static final class UsuariosEnMemoria implements UsuarioRepositoryPort {

        private final List<Usuario> usuarios = new ArrayList<>();

        @Override
        public Usuario guardar(Usuario u) {
            Usuario guardado = Usuario.reconstituir(UsuarioId.of(usuarios.size() + 1L), u.getUsername(),
                    u.getPasswordHash(), u.getRolActivo(), u.isEstadoActivo());
            usuarios.add(guardado);
            return guardado;
        }

        @Override
        public Optional<Usuario> buscarPorId(UsuarioId id) {
            return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
        }

        @Override
        public boolean existePorUsername(String username) {
            return usuarios.stream().anyMatch(u -> u.getUsername().equals(username));
        }
    }
}
