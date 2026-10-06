package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

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
