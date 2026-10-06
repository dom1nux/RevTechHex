package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes.entity.VehiculoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface SpringDataVehiculoRepository extends JpaRepository<VehiculoJpaEntity, Long> {

    Optional<VehiculoJpaEntity> findByPlaca(String placa);

    List<VehiculoJpaEntity> findByClienteId(Long clienteId);
}
