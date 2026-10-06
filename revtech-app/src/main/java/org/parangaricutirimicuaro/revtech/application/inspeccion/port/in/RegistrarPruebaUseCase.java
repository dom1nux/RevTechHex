package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

/**
 * Puerto de entrada (caso de uso): agrega el resultado de una prueba técnica (frenos, luces...) a una inspección en curso.
 */
public interface RegistrarPruebaUseCase {

    InspeccionTecnica registrar(RegistrarPruebaCommand command);

    record RegistrarPruebaCommand(Long idInspeccion, String prueba, String resultado) {}
}
