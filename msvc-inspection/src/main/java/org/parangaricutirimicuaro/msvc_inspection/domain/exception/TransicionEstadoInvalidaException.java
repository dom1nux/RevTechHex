package org.parangaricutirimicuaro.msvc_inspection.domain.exception;

/**
 * La operación solicitada no es válida para el estado actual de la inspección.
 */
public class TransicionEstadoInvalidaException extends DomainException {

    public TransicionEstadoInvalidaException(String message) {
        super(message);
    }
}
