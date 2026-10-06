package org.parangaricutirimicuaro.revtech.domain.identidad.exception;

/**
 * La operación contradice el estado actual de un usuario o rol (username duplicado, rol inactivo, usuario suspendido...).
 */
public class ReglaIdentidadVioladaException extends RuntimeException {

    public ReglaIdentidadVioladaException(String message) {
        super(message);
    }
}
