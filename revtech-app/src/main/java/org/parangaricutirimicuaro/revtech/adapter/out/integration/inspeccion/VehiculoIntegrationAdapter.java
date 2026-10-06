package org.parangaricutirimicuaro.revtech.adapter.out.integration.inspeccion;

import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarVehiculoUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.VehiculoPort;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.VehiculoId;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Puente en proceso hacia Atención a Clientes: traduce su modelo a la vista mínima que necesita Inspección.
 */
@Component
public class VehiculoIntegrationAdapter implements VehiculoPort {

    private final ConsultarVehiculoUseCase consultarVehiculo;

    public VehiculoIntegrationAdapter(ConsultarVehiculoUseCase consultarVehiculo) {
        this.consultarVehiculo = consultarVehiculo;
    }

    @Override
    public Optional<VehiculoInfo> buscarPorId(VehiculoId id) {
        return consultarVehiculo.buscarPorId(id.valor())
                .map(vehiculo -> new VehiculoInfo(id, vehiculo.getPlaca()));
    }
}
