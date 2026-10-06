package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

public interface IniciarInspeccionUseCase {

    InspeccionTecnica iniciar(Long idInspeccion);
}
