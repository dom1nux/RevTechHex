package org.parangaricutirimicuaro.revtech.adapter.out.client;

import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.VehiculoResponseDto;

public interface VehiculoClient {
    VehiculoResponseDto obtenerVehiculoPorId(Long idVehiculo);
}
