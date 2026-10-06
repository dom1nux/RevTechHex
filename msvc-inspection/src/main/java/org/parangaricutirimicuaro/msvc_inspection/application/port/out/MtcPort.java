package org.parangaricutirimicuaro.msvc_inspection.application.port.out;

import org.parangaricutirimicuaro.msvc_inspection.domain.model.CondicionInspeccion;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionId;

/**
 * Reporte normativo de resultados al Ministerio de Transportes y Comunicaciones.
 */
public interface MtcPort {

    void reportarResultado(InspeccionId inspeccion, String placa, CondicionInspeccion condicion);
}
