package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client;

import org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.dto.VehiculoResponseDto;

public interface VehiculoClient {
    VehiculoResponseDto obtenerVehiculoPorId(Long idVehiculo);
}
