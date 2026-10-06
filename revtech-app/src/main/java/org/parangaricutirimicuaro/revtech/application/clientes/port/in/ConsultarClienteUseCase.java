package org.parangaricutirimicuaro.revtech.application.clientes.port.in;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;

import java.util.Optional;

/**
 * Puerto de entrada (caso de uso): busca clientes por identificador o por documento de identidad.
 */
public interface ConsultarClienteUseCase {

    Optional<Cliente> buscarPorId(Long idCliente);

    Optional<Cliente> buscarPorDocIdent(String docIdent);
}
