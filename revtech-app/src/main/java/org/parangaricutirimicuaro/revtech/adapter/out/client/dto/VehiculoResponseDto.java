package org.parangaricutirimicuaro.revtech.adapter.out.client.dto;

public record VehiculoResponseDto(
        Long idVehiculo,
        String placa,
        String categoria,
        String marca,
        String modelo,
        Integer anioFabricacion
) {}
