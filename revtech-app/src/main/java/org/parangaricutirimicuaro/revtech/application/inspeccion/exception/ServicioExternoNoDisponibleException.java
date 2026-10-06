package org.parangaricutirimicuaro.revtech.application.inspeccion.exception;

/**
 * Un servicio de otro contexto no respondió de forma utilizable.
 */
public class ServicioExternoNoDisponibleException extends RuntimeException {

    public ServicioExternoNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
