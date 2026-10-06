package org.parangaricutirimicuaro.msvc_clientes.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.parangaricutirimicuaro.msvc_clientes.model.type.CategoriaVehiculo;

public record CrearVehiculoRequestDto(
        @NotNull(message = "clienteId es obligatorio")
        Long clienteId,

        @NotBlank(message = "placa es obligatoria")
        String placa,

        CategoriaVehiculo categoria,
        String marca,
        String modelo,
        Integer anioFabricacion
) {}
