package org.parangaricutirimicuaro.revtech.application.clientes.port.in;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;

import java.util.Optional;

public interface ConsultarVehiculoUseCase {

    Optional<Vehiculo> buscarPorId(Long idVehiculo);

    Optional<Vehiculo> buscarPorPlaca(String placa);
}
