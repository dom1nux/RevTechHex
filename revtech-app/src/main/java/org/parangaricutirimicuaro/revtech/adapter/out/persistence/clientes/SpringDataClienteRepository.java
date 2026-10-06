package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio Spring Data de clientes; solo lo usa el adaptador de persistencia.
 */
interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {

    Optional<ClienteJpaEntity> findByDocIdent(String docIdent);
}
