package org.parangaricutirimicuaro.revtech.application.identidad.port.out;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;

import java.util.Optional;

/**
 * Puerto de salida: lectura y escritura de roles de acceso en el almacenamiento.
 */
public interface RolAccesoRepositoryPort {

    RolAcceso guardar(RolAcceso rol);

    boolean existePorNombre(NombreRol nombreRol);

    Optional<RolAcceso> buscarPorId(RolId id);
}
