package org.parangaricutirimicuaro.revtech.application.identidad.port.out;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;

import java.util.Optional;

/**
 * Puerto de salida: lectura de roles de acceso desde el almacenamiento.
 */
public interface RolAccesoRepositoryPort {

    Optional<RolAcceso> buscarPorId(RolId id);
}
