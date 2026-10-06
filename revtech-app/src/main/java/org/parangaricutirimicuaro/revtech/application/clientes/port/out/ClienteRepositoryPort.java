package org.parangaricutirimicuaro.revtech.application.clientes.port.out;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;

import java.util.Optional;

/**
 * Puerto de salida: lectura y escritura de clientes en el almacenamiento.
 */
public interface ClienteRepositoryPort {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(ClienteId id);

    Optional<Cliente> buscarPorDocIdent(String docIdent);
}
