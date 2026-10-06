package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;

/**
 * Identificador tipado de vehículo: evita confundir identificadores de distintos conceptos.
 */
public record VehiculoId(Long valor) {

    public VehiculoId {
        if (valor == null || valor <= 0) {
            throw new DatoInvalidoException("Identificador de vehículo inválido: " + valor);
        }
    }

    public static VehiculoId of(Long valor) {
        return new VehiculoId(valor);
    }

    /**
     * Variante para referencias opcionales: un valor nulo representa la ausencia de referencia.
     */
    public static VehiculoId ofNullable(Long valor) {
        return valor != null ? new VehiculoId(valor) : null;
    }
}
