package org.parangaricutirimicuaro.revtech.application.clientes.port.in;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;

/**
 * Puerto de entrada (caso de uso): registra un nuevo cliente en RevTech.
 */
public interface RegistrarClienteUseCase {

    Cliente registrar(RegistrarClienteCommand command);

    record RegistrarClienteCommand(String docIdent, String nombre) {}
}
