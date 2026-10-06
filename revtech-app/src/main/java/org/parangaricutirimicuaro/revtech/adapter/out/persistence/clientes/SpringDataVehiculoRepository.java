package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes.entity.VehiculoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio Spring Data de vehículos; solo lo usa el adaptador de persistencia.
 */
interface SpringDataVehiculoRepository extends JpaRepository<VehiculoJpaEntity, Long> {

    Optional<VehiculoJpaEntity> findByPlaca(String placa);
}
