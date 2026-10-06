package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;

import java.util.Locale;

public enum TipoInspeccion {
    INSPECCION,
    REINSPECCION;

    /**
     * Interpreta el tipo recibido desde el exterior. Un valor ausente equivale a una inspección ordinaria.
     */
    public static TipoInspeccion desde(String valor) {
        if (valor == null || valor.isBlank()) {
            return INSPECCION;
        }
        try {
            return valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException("Tipo de inspección no reconocido: " + valor);
        }
    }
}
