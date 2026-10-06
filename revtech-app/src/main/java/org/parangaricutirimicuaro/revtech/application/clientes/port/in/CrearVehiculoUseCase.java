package org.parangaricutirimicuaro.revtech.application.clientes.port.in;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.CategoriaVehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;

public interface CrearVehiculoUseCase {

    Vehiculo crear(CrearVehiculoCommand command);

    record CrearVehiculoCommand(
            Long clienteId,
            String placa,
            CategoriaVehiculo categoria,
            String marca,
            String modelo,
            Integer anioFabricacion
    ) {}
}
