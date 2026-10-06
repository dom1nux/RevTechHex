package org.parangaricutirimicuaro.revtech.domain.clientes.exception;

/**
 * Un dato de entrada no cumple las reglas de construcción de un cliente o vehículo.
 */
public class DatoClienteInvalidoException extends RuntimeException {

    public DatoClienteInvalidoException(String message) {
        super(message);
    }
}
