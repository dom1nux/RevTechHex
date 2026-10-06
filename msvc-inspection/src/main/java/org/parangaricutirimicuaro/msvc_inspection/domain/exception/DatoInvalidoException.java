package org.parangaricutirimicuaro.msvc_inspection.domain.exception;

/**
 * Un dato de entrada no cumple las reglas de construcción de una entidad u objeto de valor.
 */
public class DatoInvalidoException extends DomainException {

    public DatoInvalidoException(String message) {
        super(message);
    }
}
