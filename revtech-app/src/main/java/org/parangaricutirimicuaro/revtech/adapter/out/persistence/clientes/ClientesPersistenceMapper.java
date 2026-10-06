package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes.entity.ClienteJpaEntity;
import org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes.entity.VehiculoJpaEntity;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.VehiculoId;

/**
 * Traduce clientes y vehículos de dominio a su representación JPA y viceversa.
 */
final class ClientesPersistenceMapper {

    private ClientesPersistenceMapper() {
    }

    static Cliente toDomain(ClienteJpaEntity entity) {
        return Cliente.reconstituir(ClienteId.of(entity.getIdCliente()), entity.getDocIdent(),
                entity.getNombre(), entity.getFechaRegistro());
    }

    static VehiculoJpaEntity toEntity(Vehiculo vehiculo) {
        VehiculoJpaEntity entity = new VehiculoJpaEntity();
        entity.setIdVehiculo(vehiculo.getId() != null ? vehiculo.getId().valor() : null);
        entity.setClienteId(vehiculo.getPropietario().valor());
        entity.setPlaca(vehiculo.getPlaca());
        entity.setCategoria(vehiculo.getCategoria());
        entity.setMarca(vehiculo.getMarca());
        entity.setModelo(vehiculo.getModelo());
        entity.setAnioFabricacion(vehiculo.getAnioFabricacion());
        return entity;
    }

    static Vehiculo toDomain(VehiculoJpaEntity entity) {
        return Vehiculo.reconstituir(VehiculoId.of(entity.getIdVehiculo()), ClienteId.of(entity.getClienteId()),
                entity.getPlaca(), entity.getCategoria(), entity.getMarca(), entity.getModelo(),
                entity.getAnioFabricacion());
    }
}
