package org.parangaricutirimicuaro.msvc_inspection.application.port.out;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.VehiculoId;

import java.util.Optional;

/**
 * Consulta de vehículos en el contexto Atención a Clientes.
 */
public interface VehiculoPort {

    Optional<VehiculoInfo> buscarPorId(VehiculoId id);

    record VehiculoInfo(VehiculoId id, String placa) {}
}
