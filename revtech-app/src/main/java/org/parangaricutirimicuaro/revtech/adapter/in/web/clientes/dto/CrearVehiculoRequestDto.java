package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.CategoriaVehiculo;

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
