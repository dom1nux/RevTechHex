package org.parangaricutirimicuaro.msvc_inspection.domain.model;

import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;

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
