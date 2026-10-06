package org.parangaricutirimicuaro.revtech.domain.identidad.model;

import org.parangaricutirimicuaro.revtech.domain.identidad.exception.DatoIdentidadInvalidoException;

/**
 * Identificador tipado de rol de acceso.
 */
public record RolId(Long valor) {

    public RolId {
        if (valor == null || valor <= 0) {
            throw new DatoIdentidadInvalidoException("Identificador de rol inválido: " + valor);
        }
    }

    public static RolId of(Long valor) {
        return new RolId(valor);
    }

    /**
     * Variante para referencias opcionales: un valor nulo representa la ausencia de rol.
     */
    public static RolId ofNullable(Long valor) {
        return valor != null ? new RolId(valor) : null;
    }
}
