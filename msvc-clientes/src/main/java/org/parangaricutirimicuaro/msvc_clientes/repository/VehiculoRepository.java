package org.parangaricutirimicuaro.msvc_clientes.repository;

import org.parangaricutirimicuaro.msvc_clientes.model.entity.Vehiculo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends CrudRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPlaca(String placa);
    List<Vehiculo> findByClienteId(Long clienteId);
}
