package org.parangaricutirimicuaro.msvc_inspection.application.port.out;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionId;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionTecnica;

import java.util.Optional;

public interface InspeccionRepositoryPort {

    InspeccionTecnica guardar(InspeccionTecnica inspeccion);

    Optional<InspeccionTecnica> buscarPorId(InspeccionId id);
}
