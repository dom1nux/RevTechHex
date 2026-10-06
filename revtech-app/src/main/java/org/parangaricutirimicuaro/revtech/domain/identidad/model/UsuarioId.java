package org.parangaricutirimicuaro.revtech.domain.identidad.model;

import org.parangaricutirimicuaro.revtech.domain.identidad.exception.DatoIdentidadInvalidoException;

/**
 * Identificador tipado de usuario.
 */
public record UsuarioId(Long valor) {

    public UsuarioId {
        if (valor == null || valor <= 0) {
            throw new DatoIdentidadInvalidoException("Identificador de usuario inválido: " + valor);
        }
    }

    public static UsuarioId of(Long valor) {
        return new UsuarioId(valor);
    }
}
