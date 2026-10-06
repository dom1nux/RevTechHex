package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client;

import org.parangaricutirimicuaro.msvc_inspection.application.port.out.VehiculoPort;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.VehiculoId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VehiculoClientAdapter implements VehiculoPort {

    private final VehiculoClient client;

    public VehiculoClientAdapter(VehiculoClient client) {
        this.client = client;
    }

    @Override
    public Optional<VehiculoInfo> buscarPorId(VehiculoId id) {
        return ExternalCalls.consultar("msvc-clientes", () -> client.obtenerVehiculoPorId(id.valor()))
                .map(dto -> new VehiculoInfo(id, dto.placa()));
    }
}
