package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes;

import org.parangaricutirimicuaro.revtech.application.clientes.port.out.VehiculoRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.VehiculoId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador de salida: implementa {@code VehiculoRepositoryPort} con JPA.
 */
@Component
@Transactional(readOnly = true)
public class VehiculoPersistenceAdapter implements VehiculoRepositoryPort {

    private final SpringDataVehiculoRepository repository;

    VehiculoPersistenceAdapter(SpringDataVehiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Vehiculo guardar(Vehiculo vehiculo) {
        return ClientesPersistenceMapper.toDomain(repository.save(ClientesPersistenceMapper.toEntity(vehiculo)));
    }

    @Override
    public Optional<Vehiculo> buscarPorId(VehiculoId id) {
        return repository.findById(id.valor()).map(ClientesPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        return repository.findByPlaca(placa).map(ClientesPersistenceMapper::toDomain);
    }
}
