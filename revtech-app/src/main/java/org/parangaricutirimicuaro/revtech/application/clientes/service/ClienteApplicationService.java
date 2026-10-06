package org.parangaricutirimicuaro.revtech.application.clientes.service;

import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarClienteUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.out.ClienteRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;

import java.util.Optional;

public class ClienteApplicationService implements ConsultarClienteUseCase {

    private final ClienteRepositoryPort repository;

    public ClienteApplicationService(ClienteRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Cliente> buscarPorId(Long idCliente) {
        return repository.buscarPorId(ClienteId.of(idCliente));
    }

    @Override
    public Optional<Cliente> buscarPorDocIdent(String docIdent) {
        return repository.buscarPorDocIdent(docIdent);
    }
}
