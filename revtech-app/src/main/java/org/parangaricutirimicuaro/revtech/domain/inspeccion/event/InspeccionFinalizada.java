package org.parangaricutirimicuaro.revtech.domain.inspeccion.event;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CondicionInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.VehiculoId;

import java.time.LocalDateTime;

/**
 * Evento de dominio: una inspección terminó con la condición indicada. Lo registra el agregado y se publica después de guardarlo.
 */
public record InspeccionFinalizada(
        InspeccionId inspeccion,
        VehiculoId vehiculo,
        CondicionInspeccion condicion,
        LocalDateTime ocurridoEn
) implements EventoDominio {}
