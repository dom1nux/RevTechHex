package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes;

import org.parangaricutirimicuaro.revtech.application.clientes.port.out.ClienteRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final SpringDataClienteRepository repository;

    ClientePersistenceAdapter(SpringDataClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Cliente> buscarPorId(ClienteId id) {
        return repository.findById(id.valor()).map(ClientesPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorDocIdent(String docIdent) {
        return repository.findByDocIdent(docIdent).map(ClientesPersistenceMapper::toDomain);
    }
}
