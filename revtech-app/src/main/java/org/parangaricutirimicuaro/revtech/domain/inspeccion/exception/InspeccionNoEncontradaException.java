package org.parangaricutirimicuaro.revtech.domain.inspeccion.exception;

/**
 * No existe una inspección con el identificador solicitado (HTTP 404).
 */
public class InspeccionNoEncontradaException extends DomainException {

    public InspeccionNoEncontradaException(Long idInspeccion) {
        super("Inspección técnica no encontrada con ID: " + idInspeccion);
    }
}
