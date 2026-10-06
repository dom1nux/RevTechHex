package org.parangaricutirimicuaro.msvc_inspection.application.port.in;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionTecnica;

public interface RegistrarPruebaUseCase {

    InspeccionTecnica registrar(RegistrarPruebaCommand command);

    record RegistrarPruebaCommand(Long idInspeccion, String prueba, String resultado) {}
}
