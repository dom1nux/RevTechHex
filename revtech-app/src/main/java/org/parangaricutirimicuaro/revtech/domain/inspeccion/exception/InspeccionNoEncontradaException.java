package org.parangaricutirimicuaro.revtech.domain.inspeccion.exception;

public class InspeccionNoEncontradaException extends DomainException {

    public InspeccionNoEncontradaException(Long idInspeccion) {
        super("Inspección técnica no encontrada con ID: " + idInspeccion);
    }
}
