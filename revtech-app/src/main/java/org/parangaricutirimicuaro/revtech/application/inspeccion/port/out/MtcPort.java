package org.parangaricutirimicuaro.revtech.application.inspeccion.port.out;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CondicionInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;

/**
 * Reporte normativo de resultados al Ministerio de Transportes y Comunicaciones.
 */
public interface MtcPort {

    void reportarResultado(InspeccionId inspeccion, String placa, CondicionInspeccion condicion);
}
