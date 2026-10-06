package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

public interface RegistrarPruebaUseCase {

    InspeccionTecnica registrar(RegistrarPruebaCommand command);

    record RegistrarPruebaCommand(Long idInspeccion, String prueba, String resultado) {}
}
