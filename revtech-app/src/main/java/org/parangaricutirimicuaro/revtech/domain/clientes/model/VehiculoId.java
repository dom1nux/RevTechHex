package org.parangaricutirimicuaro.revtech.domain.clientes.model;

import org.parangaricutirimicuaro.revtech.domain.clientes.exception.DatoClienteInvalidoException;

/**
 * Identificador tipado de vehículo dentro del contexto Atención a Clientes.
 */
public record VehiculoId(Long valor) {

    public VehiculoId {
        if (valor == null || valor <= 0) {
            throw new DatoClienteInvalidoException("Identificador de vehículo inválido: " + valor);
        }
    }

    public static VehiculoId of(Long valor) {
        return new VehiculoId(valor);
    }
}
