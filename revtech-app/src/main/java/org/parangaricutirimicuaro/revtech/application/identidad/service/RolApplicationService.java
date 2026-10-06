package org.parangaricutirimicuaro.revtech.application.identidad.service;

import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarRolUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.RolAccesoRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.ReglaIdentidadVioladaException;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;

/**
 * Servicio de aplicación de roles: implementa los casos de uso usando solo puertos. Se registra como bean en {@code IdentidadConfig}.
 */
public class RolApplicationService implements RegistrarRolUseCase {

    private final RolAccesoRepositoryPort roles;

    public RolApplicationService(RolAccesoRepositoryPort roles) {
        this.roles = roles;
    }

    @Override
    public RolAcceso registrar(RegistrarRolCommand command) {
        RolAcceso nuevo = RolAcceso.registrar(command.nombreRol(), command.permisos());
        if (roles.existePorNombre(nuevo.getNombreRol())) {
            throw new ReglaIdentidadVioladaException("El rol '" + nuevo.getNombreRol() + "' ya existe");
        }
        return roles.guardar(nuevo);
    }
}
