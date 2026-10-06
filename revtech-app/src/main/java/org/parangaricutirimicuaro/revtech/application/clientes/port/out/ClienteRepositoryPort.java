package org.parangaricutirimicuaro.revtech.application.clientes.port.out;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;

import java.util.Optional;

/**
 * Puerto de salida: lectura de clientes desde el almacenamiento.
 */
public interface ClienteRepositoryPort {

    Optional<Cliente> buscarPorId(ClienteId id);

    Optional<Cliente> buscarPorDocIdent(String docIdent);
}
