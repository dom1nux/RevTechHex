package org.parangaricutirimicuaro.msvc_inspection.domain.exception;

public class InspeccionNoEncontradaException extends DomainException {

    public InspeccionNoEncontradaException(Long idInspeccion) {
        super("Inspección técnica no encontrada con ID: " + idInspeccion);
    }
}
