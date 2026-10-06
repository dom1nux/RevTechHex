package org.parangaricutirimicuaro.revtech.domain.identidad.exception;

/**
 * Un dato de entrada no cumple las reglas de construcción de un usuario o rol.
 */
public class DatoIdentidadInvalidoException extends RuntimeException {

    public DatoIdentidadInvalidoException(String message) {
        super(message);
    }
}
