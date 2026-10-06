package org.parangaricutirimicuaro.revtech.application.clientes.port.in;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;

import java.util.Optional;

public interface ConsultarClienteUseCase {

    Optional<Cliente> buscarPorId(Long idCliente);

    Optional<Cliente> buscarPorDocIdent(String docIdent);
}
