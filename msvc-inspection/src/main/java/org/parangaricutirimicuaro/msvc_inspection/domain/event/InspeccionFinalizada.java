package org.parangaricutirimicuaro.msvc_inspection.domain.event;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.CondicionInspeccion;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionId;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.VehiculoId;

import java.time.LocalDateTime;

public record InspeccionFinalizada(
        InspeccionId inspeccion,
        VehiculoId vehiculo,
        CondicionInspeccion condicion,
        LocalDateTime ocurridoEn
) implements EventoDominio {}
