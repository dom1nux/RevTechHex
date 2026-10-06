package org.parangaricutirimicuaro.msvc_clientes.repository;

import org.parangaricutirimicuaro.msvc_clientes.model.entity.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
    Optional<Cliente> findByDocIdent(String docIdent);
}
