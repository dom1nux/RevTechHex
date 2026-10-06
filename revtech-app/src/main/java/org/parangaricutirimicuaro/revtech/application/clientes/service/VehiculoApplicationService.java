package org.parangaricutirimicuaro.revtech.application.clientes.service;

import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarVehiculoUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.CrearVehiculoUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.out.VehiculoRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.VehiculoId;

import java.util.Optional;

public class VehiculoApplicationService implements CrearVehiculoUseCase, ConsultarVehiculoUseCase {

    private final VehiculoRepositoryPort repository;

    public VehiculoApplicationService(VehiculoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Vehiculo crear(CrearVehiculoCommand command) {
        Vehiculo vehiculo = Vehiculo.registrar(
                ClienteId.of(command.clienteId()),
                command.placa(),
                command.categoria(),
                command.marca(),
                command.modelo(),
                command.anioFabricacion());
        return repository.guardar(vehiculo);
    }

    @Override
    public Optional<Vehiculo> buscarPorId(Long idVehiculo) {
        return repository.buscarPorId(VehiculoId.of(idVehiculo));
    }

    @Override
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        return repository.buscarPorPlaca(placa);
    }
}
