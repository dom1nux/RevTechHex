package org.parangaricutirimicuaro.revtech.domain.identidad.exception;

/**
 * El usuario o rol referenciado no existe.
 */
public class RecursoIdentidadNoEncontradoException extends RuntimeException {

    public RecursoIdentidadNoEncontradoException(String message) {
        super(message);
    }
}
