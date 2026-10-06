package org.parangaricutirimicuaro.revtech.application.inspeccion.port.out;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.VehiculoId;

import java.util.Optional;

/**
 * Consulta de vehículos en el contexto Atención a Clientes.
 */
public interface VehiculoPort {

    Optional<VehiculoInfo> buscarPorId(VehiculoId id);

    record VehiculoInfo(VehiculoId id, String placa) {}
}
