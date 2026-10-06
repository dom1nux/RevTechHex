package org.parangaricutirimicuaro.msvc_inspection.application.port.in;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionTecnica;

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
