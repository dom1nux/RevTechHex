package org.parangaricutirimicuaro.revtech.application.clientes.service;

import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarClienteUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.RegistrarClienteUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.out.ClienteRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.clientes.exception.ReglaClienteVioladaException;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio de aplicación de clientes: implementa los casos de uso usando solo puertos. Se registra como bean en {@code ClientesConfig}.
 */
public class ClienteApplicationService implements RegistrarClienteUseCase, ConsultarClienteUseCase {

    private final ClienteRepositoryPort repository;

    private final Clock clock;

    public ClienteApplicationService(ClienteRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Cliente registrar(RegistrarClienteCommand command) {
        Cliente nuevo = Cliente.registrar(command.docIdent(), command.nombre(), LocalDateTime.now(clock));
        if (repository.buscarPorDocIdent(nuevo.getDocIdent()).isPresent()) {
            throw new ReglaClienteVioladaException(
                    "Ya existe un cliente con documento de identidad '" + nuevo.getDocIdent() + "'");
        }
        return repository.guardar(nuevo);
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
