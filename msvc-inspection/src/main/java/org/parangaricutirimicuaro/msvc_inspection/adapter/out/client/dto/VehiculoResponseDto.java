package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.dto;

public record VehiculoResponseDto(
        Long idVehiculo,
        String placa,
        String categoria,
        String marca,
        String modelo,
        Integer anioFabricacion
) {}
