package org.parangaricutirimicuaro.revtech.application.identidad.port.in;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;

import java.util.List;

/**
 * Puerto de entrada (caso de uso): da de alta un rol de acceso con sus permisos.
 */
public interface RegistrarRolUseCase {

    RolAcceso registrar(RegistrarRolCommand command);

    record RegistrarRolCommand(NombreRol nombreRol, List<String> permisos) {}
}
