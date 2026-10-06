package org.parangaricutirimicuaro.revtech.application.inspeccion.port.in;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.event.InspeccionFinalizada;

/**
 * Reacción al evento {@link InspeccionFinalizada}: informar el resultado al MTC.
 */
public interface ReportarResultadoMtcUseCase {

    void alFinalizarInspeccion(InspeccionFinalizada evento);
}
