package org.parangaricutirimicuaro.revtech.application.clientes.port.out;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.VehiculoId;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepositoryPort {

    Vehiculo guardar(Vehiculo vehiculo);

    Optional<Vehiculo> buscarPorId(VehiculoId id);

    Optional<Vehiculo> buscarPorPlaca(String placa);

    List<Vehiculo> listarPorPropietario(ClienteId propietario);
}
