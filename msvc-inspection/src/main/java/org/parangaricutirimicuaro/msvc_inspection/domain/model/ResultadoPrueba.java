package org.parangaricutirimicuaro.msvc_inspection.domain.model;

import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;

/**
 * Objeto de valor: veredicto obtenido en una prueba técnica concreta.
 */
public record ResultadoPrueba(TipoPrueba prueba, VeredictoPrueba resultado) {

    public ResultadoPrueba {
        if (prueba == null) {
            throw new DatoInvalidoException("El tipo de prueba es obligatorio");
        }
        if (resultado == null) {
            throw new DatoInvalidoException("El resultado de la prueba es obligatorio");
        }
    }

    public static ResultadoPrueba de(String prueba, String resultado) {
        return new ResultadoPrueba(TipoPrueba.desde(prueba), VeredictoPrueba.desde(resultado));
    }

    public boolean esDeficiente() {
        return resultado.esDeficiente();
    }
}
