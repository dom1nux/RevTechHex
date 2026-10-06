package org.parangaricutirimicuaro.msvc_inspection.application.port.in;

import org.parangaricutirimicuaro.msvc_inspection.domain.event.InspeccionFinalizada;

/**
 * Reacción al evento {@link InspeccionFinalizada}: informar el resultado al MTC.
 */
public interface ReportarResultadoMtcUseCase {

    void alFinalizarInspeccion(InspeccionFinalizada evento);
}
