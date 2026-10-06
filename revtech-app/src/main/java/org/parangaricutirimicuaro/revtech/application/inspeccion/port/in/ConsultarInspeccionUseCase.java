package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;

public interface ConsultarInspeccionUseCase {

    InspeccionTecnica obtenerPorId(Long idInspeccion);
}
