package org.parangaricutirimicuaro.revtech.adapter.out.client.feign;

import org.parangaricutirimicuaro.revtech.adapter.out.client.VehiculoClient;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.VehiculoResponseDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-clientes", url = "${clients.msvc-clientes.url:http://localhost:8081}")
@ConditionalOnProperty(name = "revtech.clients.mock", havingValue = "false")
public interface FeignVehiculoClient extends VehiculoClient {

    @Override
    @GetMapping("/api/vehiculos/{idVehiculo}")
    VehiculoResponseDto obtenerVehiculoPorId(@PathVariable("idVehiculo") Long idVehiculo);
}
