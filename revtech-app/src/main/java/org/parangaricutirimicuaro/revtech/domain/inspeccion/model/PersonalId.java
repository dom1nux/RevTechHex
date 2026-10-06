package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;

/**
 * Identificador tipado de personal técnico: evita confundir identificadores de distintos conceptos.
 */
public record PersonalId(Long valor) {

    public PersonalId {
        if (valor == null || valor <= 0) {
            throw new DatoInvalidoException("Identificador de personal técnico inválido: " + valor);
        }
    }

    public static PersonalId of(Long valor) {
        return new PersonalId(valor);
    }

    /**
     * Variante para referencias opcionales: un valor nulo representa la ausencia de referencia.
     */
    public static PersonalId ofNullable(Long valor) {
        return valor != null ? new PersonalId(valor) : null;
    }
}
