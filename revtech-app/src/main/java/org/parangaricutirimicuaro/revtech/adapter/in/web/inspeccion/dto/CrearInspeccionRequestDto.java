package org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion.dto;

import jakarta.validation.constraints.NotNull;

public record CrearInspeccionRequestDto(
        @NotNull(message = "El id del vehículo es obligatorio")
        Long idVehiculo,

        @NotNull(message = "El id del inspector es obligatorio")
        Long idInspector,

        Long idSupervisor,

        String tipoInspeccion,

        Long idInspeccionOrigen
) {}
