package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

/**
 * Puerto de entrada (caso de uso): registra una inspección o una reinspección, validando que el vehículo y el personal existan.
 */
public interface CrearInspeccionUseCase {

    InspeccionTecnica crear(CrearInspeccionCommand command);

    record CrearInspeccionCommand(
            Long idVehiculo,
            Long idInspector,
            Long idSupervisor,
            String tipoInspeccion,
            Long idInspeccionOrigen
    ) {}
}
