package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

/**
 * Puerto de entrada (caso de uso): obtiene una inspección por su identificador.
 */
public interface ConsultarInspeccionUseCase {

    InspeccionTecnica obtenerPorId(Long idInspeccion);
}
