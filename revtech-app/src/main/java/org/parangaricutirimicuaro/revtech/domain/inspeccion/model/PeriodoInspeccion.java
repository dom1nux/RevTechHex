package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;

import java.time.LocalDateTime;

/**
 * Objeto de valor: intervalo temporal en que se ejecuta la inspección.
 */
public record PeriodoInspeccion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {

    public PeriodoInspeccion {
        if (fechaFin != null && fechaInicio == null) {
            throw new DatoInvalidoException("Un periodo no puede tener fecha de fin sin fecha de inicio");
        }
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new DatoInvalidoException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    public static PeriodoInspeccion iniciadoEn(LocalDateTime fechaInicio) {
        return new PeriodoInspeccion(fechaInicio, null);
    }

    public PeriodoInspeccion finalizadoEn(LocalDateTime fechaFin) {
        return new PeriodoInspeccion(fechaInicio, fechaFin);
    }
}
