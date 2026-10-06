package org.parangaricutirimicuaro.revtech.adapter.out.client.mock;

import org.parangaricutirimicuaro.revtech.adapter.out.client.VehiculoClient;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.VehiculoResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "revtech.clients.mock", havingValue = "true", matchIfMissing = true)
public class MockVehiculoClient implements VehiculoClient {

    private static final Logger log = LoggerFactory.getLogger(MockVehiculoClient.class);

    @Override
    public VehiculoResponseDto obtenerVehiculoPorId(Long idVehiculo) {
        log.info("[MOCK] Consultando vehículo con id: {}", idVehiculo);

        if (idVehiculo == null) {
            throw new IllegalArgumentException("El id del vehículo no puede ser nulo");
        }

        if (idVehiculo == 999L) {
            log.info("[MOCK] Vehículo {} no existe en msvc-clientes (simulado)", idVehiculo);
            return null;
        }

        if (idVehiculo == 2L) {
            return new VehiculoResponseDto(
                    idVehiculo,
                    "XYZ-789",
                    "N3",
                    "Volvo",
                    "FH",
                    2022
            );
        }

        return new VehiculoResponseDto(
                idVehiculo,
                "ABC-123",
                "M1",
                "Toyota",
                "Corolla",
                2020
        );
    }
}
