package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;

public record VehiculoResponseDto(
        Long idVehiculo,
        String placa,
        String categoria,
        String marca,
        String modelo,
        Integer anioFabricacion,
        Long clienteId
) {
    public static VehiculoResponseDto from(Vehiculo v) {
        return new VehiculoResponseDto(
                v.getId().valor(),
                v.getPlaca(),
                v.getCategoria() != null ? v.getCategoria().name() : null,
                v.getMarca(),
                v.getModelo(),
                v.getAnioFabricacion(),
                v.getPropietario().valor()
        );
    }
}
