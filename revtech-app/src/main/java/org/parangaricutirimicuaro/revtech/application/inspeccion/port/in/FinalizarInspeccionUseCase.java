package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

public interface FinalizarInspeccionUseCase {

    InspeccionTecnica finalizar(Long idInspeccion, String observaciones);
}
