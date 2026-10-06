package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

/**
 * Puerto de entrada (caso de uso): cierra la inspección; el agregado decide si emite certificado o acta.
 */
public interface FinalizarInspeccionUseCase {

    InspeccionTecnica finalizar(Long idInspeccion, String observaciones);
}
