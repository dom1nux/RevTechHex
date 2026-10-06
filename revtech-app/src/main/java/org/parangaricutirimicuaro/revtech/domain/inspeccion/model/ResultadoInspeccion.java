package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;

/**
 * Objeto de valor: condición final del vehículo tras evaluar todas las pruebas (Apto u Observado).
 */
public record ResultadoInspeccion(CondicionInspeccion condicion) {

    public static final ResultadoInspeccion APTO = new ResultadoInspeccion(CondicionInspeccion.APTO);
    public static final ResultadoInspeccion OBSERVADO = new ResultadoInspeccion(CondicionInspeccion.OBSERVADO);

    public ResultadoInspeccion {
        if (condicion == null) {
            throw new DatoInvalidoException("La condición del resultado es obligatoria");
        }
    }

    public boolean esApto() {
        return condicion == CondicionInspeccion.APTO;
    }
}
