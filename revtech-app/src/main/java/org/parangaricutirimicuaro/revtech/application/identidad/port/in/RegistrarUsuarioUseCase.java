package org.parangaricutirimicuaro.revtech.application.identidad.port.in;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;

public interface RegistrarUsuarioUseCase {

    Usuario registrar(RegistrarUsuarioCommand command);

    record RegistrarUsuarioCommand(String username, String password, Long rolActivoId) {}
}
