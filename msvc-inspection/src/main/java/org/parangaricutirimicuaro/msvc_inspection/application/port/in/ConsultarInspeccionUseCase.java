package org.parangaricutirimicuaro.msvc_inspection.application.port.in;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionTecnica;

public interface ConsultarInspeccionUseCase {

    InspeccionTecnica obtenerPorId(Long idInspeccion);
}
