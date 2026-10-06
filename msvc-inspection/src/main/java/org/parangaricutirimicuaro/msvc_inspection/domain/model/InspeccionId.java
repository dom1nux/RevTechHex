package org.parangaricutirimicuaro.msvc_inspection.domain.model;

import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;

/**
 * Identificador tipado de inspección: evita confundir identificadores de distintos conceptos.
 */
public record InspeccionId(Long valor) {

    public InspeccionId {
        if (valor == null || valor <= 0) {
            throw new DatoInvalidoException("Identificador de inspección inválido: " + valor);
        }
    }

    public static InspeccionId of(Long valor) {
        return new InspeccionId(valor);
    }

    /**
     * Variante para referencias opcionales: un valor nulo representa la ausencia de referencia.
     */
    public static InspeccionId ofNullable(Long valor) {
        return valor != null ? new InspeccionId(valor) : null;
    }
}
