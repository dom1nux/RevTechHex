package org.parangaricutirimicuaro.revtech.application.identidad.port.in;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;

/**
 * Puerto de entrada (caso de uso): crea una cuenta de usuario con un rol existente y activo.
 */
public interface RegistrarUsuarioUseCase {

    Usuario registrar(RegistrarUsuarioCommand command);

    record RegistrarUsuarioCommand(String username, String password, Long rolActivoId) {}
}
