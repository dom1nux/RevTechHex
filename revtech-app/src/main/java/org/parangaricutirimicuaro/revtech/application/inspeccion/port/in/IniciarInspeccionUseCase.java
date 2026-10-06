package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

/**
 * Puerto de entrada (caso de uso): pasa una inspección de REGISTRADA a EN_PROCESO y marca la hora de inicio.
 */
public interface IniciarInspeccionUseCase {

    InspeccionTecnica iniciar(Long idInspeccion);
}
