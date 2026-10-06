package org.parangaricutirimicuaro.revtech.application.inspeccion.exception;

/**
 * Un identificador recibido apunta a un recurso de otro contexto que no existe o no es utilizable.
 */
public class ReferenciaExternaInvalidaException extends RuntimeException {

    public ReferenciaExternaInvalidaException(String message) {
        super(message);
    }
}
