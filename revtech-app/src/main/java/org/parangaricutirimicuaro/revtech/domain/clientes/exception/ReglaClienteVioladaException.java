package org.parangaricutirimicuaro.revtech.domain.clientes.exception;

/**
 * Una operación sobre clientes o vehículos viola una regla de negocio (por ejemplo, un documento ya registrado).
 */
public class ReglaClienteVioladaException extends RuntimeException {

    public ReglaClienteVioladaException(String message) {
        super(message);
    }
}
