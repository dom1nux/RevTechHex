package org.parangaricutirimicuaro.revtech.domain.inspeccion.exception;

/**
 * La operación es coherente en forma, pero contradice una regla del negocio de inspecciones.
 */
public class ReglaNegocioVioladaException extends DomainException {

    public ReglaNegocioVioladaException(String message) {
        super(message);
    }
}
