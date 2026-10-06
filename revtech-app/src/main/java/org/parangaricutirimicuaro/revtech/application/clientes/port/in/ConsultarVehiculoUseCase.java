package org.parangaricutirimicuaro.revtech.application.clientes.port.in;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;

import java.util.Optional;

/**
 * Puerto de entrada (caso de uso): busca vehículos. Además de la API REST, lo usa Inspección para validar vehículos.
 */
public interface ConsultarVehiculoUseCase {

    Optional<Vehiculo> buscarPorId(Long idVehiculo);

    Optional<Vehiculo> buscarPorPlaca(String placa);
}
