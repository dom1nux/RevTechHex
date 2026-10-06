package org.parangaricutirimicuaro.revtech.domain.clientes.model;

import org.parangaricutirimicuaro.revtech.domain.clientes.exception.DatoClienteInvalidoException;

/**
 * Identificador tipado de cliente.
 */
public record ClienteId(Long valor) {

    public ClienteId {
        if (valor == null || valor <= 0) {
            throw new DatoClienteInvalidoException("Identificador de cliente inválido: " + valor);
        }
    }

    public static ClienteId of(Long valor) {
        return new ClienteId(valor);
    }
}
