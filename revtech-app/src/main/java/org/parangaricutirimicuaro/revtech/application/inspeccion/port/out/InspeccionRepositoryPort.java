package org.parangaricutirimicuaro.revtech.application.inspeccion.port.out;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

import java.util.Optional;

/**
 * Puerto de salida: guarda y recupera el agregado {@code InspeccionTecnica} sin que la aplicación sepa que existe JPA.
 */
public interface InspeccionRepositoryPort {

    InspeccionTecnica guardar(InspeccionTecnica inspeccion);

    Optional<InspeccionTecnica> buscarPorId(InspeccionId id);
}
