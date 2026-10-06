package org.parangaricutirimicuaro.revtech.application.inspeccion.service;

import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.ReportarResultadoMtcUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.MtcPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.VehiculoPort;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.event.InspeccionFinalizada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reporta al MTC cada inspección finalizada. Es de mejor esfuerzo: un fallo externo no revierte la
 * inspección, que ya fue persistida cuando se publica el evento.
 */
public class ReportarResultadoMtcService implements ReportarResultadoMtcUseCase {

    private static final Logger log = LoggerFactory.getLogger(ReportarResultadoMtcService.class);
    static final String PLACA_DESCONOCIDA = "DESCONOCIDA";

    private final VehiculoPort vehiculoPort;
    private final MtcPort mtcPort;

    public ReportarResultadoMtcService(VehiculoPort vehiculoPort, MtcPort mtcPort) {
        this.vehiculoPort = vehiculoPort;
        this.mtcPort = mtcPort;
    }

    @Override
    public void alFinalizarInspeccion(InspeccionFinalizada evento) {
        try {
            String placa = vehiculoPort.buscarPorId(evento.vehiculo())
                    .map(VehiculoPort.VehiculoInfo::placa)
                    .orElse(PLACA_DESCONOCIDA);
            mtcPort.reportarResultado(evento.inspeccion(), placa, evento.condicion());
        } catch (RuntimeException e) {
            log.warn("Alerta al reportar al MTC la inspección {}: {}", evento.inspeccion().valor(), e.getMessage());
        }
    }
}
