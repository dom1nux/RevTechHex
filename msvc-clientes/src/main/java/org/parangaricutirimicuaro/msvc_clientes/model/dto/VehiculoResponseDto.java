package org.parangaricutirimicuaro.msvc_clientes.model.dto;

import org.parangaricutirimicuaro.msvc_clientes.model.entity.Vehiculo;

public record VehiculoResponseDto(
        Long idVehiculo,
        String placa,
        String categoria,
        String marca,
        String modelo,
        Integer anioFabricacion,
        Long clienteId
) {
    public static VehiculoResponseDto fromEntity(Vehiculo v) {
        return new VehiculoResponseDto(
                v.getIdVehiculo(),
                v.getPlaca(),
                v.getCategoria() != null ? v.getCategoria().name() : null,
                v.getMarca(),
                v.getModelo(),
                v.getAnioFabricacion(),
                v.getClienteId()
        );
    }
}
