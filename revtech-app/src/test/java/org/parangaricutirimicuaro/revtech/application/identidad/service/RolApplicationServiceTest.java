package org.parangaricutirimicuaro.revtech.application.identidad.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarRolUseCase.RegistrarRolCommand;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.RolAccesoRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.DatoIdentidadInvalidoException;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.ReglaIdentidadVioladaException;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RolApplicationServiceTest {

    private final EnMemoria repository = new EnMemoria();
    private final RolApplicationService service = new RolApplicationService(repository);

    @Test
    @DisplayName("Registra el rol activo con sus permisos y le asigna un id")
    void registra() {
        RolAcceso creado = service.registrar(new RegistrarRolCommand(NombreRol.ROLE_INSPECTOR, List.of("inspecciones:escribir")));

        assertThat(creado.getId()).isNotNull();
        assertThat(creado.getNombreRol()).isEqualTo(NombreRol.ROLE_INSPECTOR);
        assertThat(creado.getPermisos()).containsExactly("inspecciones:escribir");
        assertThat(creado.isEstadoActivo()).isTrue();
    }

    @Test
    @DisplayName("Rechaza un rol que ya existe")
    void duplicado() {
        service.registrar(new RegistrarRolCommand(NombreRol.ROLE_ADMIN, List.of()));

        assertThatThrownBy(() -> service.registrar(new RegistrarRolCommand(NombreRol.ROLE_ADMIN, List.of())))
                .isInstanceOf(ReglaIdentidadVioladaException.class);
    }

    @Test
    @DisplayName("Rechaza un rol sin nombre")
    void sinNombre() {
        assertThatThrownBy(() -> service.registrar(new RegistrarRolCommand(null, List.of())))
                .isInstanceOf(DatoIdentidadInvalidoException.class);
    }

    private static class EnMemoria implements RolAccesoRepositoryPort {
        private final List<RolAcceso> datos = new ArrayList<>();

        @Override
        public RolAcceso guardar(RolAcceso rol) {
            RolAcceso guardado = RolAcceso.reconstituir(RolId.of((long) datos.size() + 1), rol.getNombreRol(),
                    rol.getPermisos(), rol.isEstadoActivo());
            datos.add(guardado);
            return guardado;
        }

        @Override
        public boolean existePorNombre(NombreRol nombreRol) {
            return datos.stream().anyMatch(r -> r.getNombreRol() == nombreRol);
        }

        @Override
        public Optional<RolAcceso> buscarPorId(RolId id) {
            return datos.stream().filter(r -> r.getId().equals(id)).findFirst();
        }
    }
}
