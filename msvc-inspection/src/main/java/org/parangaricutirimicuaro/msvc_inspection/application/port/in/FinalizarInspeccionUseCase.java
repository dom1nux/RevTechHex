package org.parangaricutirimicuaro.msvc_inspection.application.port.in;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionTecnica;

public interface FinalizarInspeccionUseCase {

    InspeccionTecnica finalizar(Long idInspeccion, String observaciones);
}
