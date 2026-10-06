package org.parangaricutirimicuaro.revtech.domain.inspeccion.exception;

/**
 * Raíz de las excepciones que expresan violaciones de reglas del dominio de Inspección.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
