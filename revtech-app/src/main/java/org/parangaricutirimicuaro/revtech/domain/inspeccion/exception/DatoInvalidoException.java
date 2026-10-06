package org.parangaricutirimicuaro.revtech.domain.inspeccion.exception;

/**
 * Un dato de entrada no cumple las reglas de construcción de una entidad u objeto de valor.
 */
public class DatoInvalidoException extends DomainException {

    public DatoInvalidoException(String message) {
        super(message);
    }
}
