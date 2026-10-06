package org.parangaricutirimicuaro.msvc_inspection.adapter.in.event;

import org.parangaricutirimicuaro.msvc_inspection.application.port.in.ReportarResultadoMtcUseCase;
import org.parangaricutirimicuaro.msvc_inspection.domain.event.InspeccionFinalizada;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada: traduce los eventos de dominio publicados en Spring a casos de uso de la aplicación.
 */
@Component
public class InspeccionEventListener {

    private final ReportarResultadoMtcUseCase reportarResultadoMtc;

    public InspeccionEventListener(ReportarResultadoMtcUseCase reportarResultadoMtc) {
        this.reportarResultadoMtc = reportarResultadoMtc;
    }

    @EventListener
    public void on(InspeccionFinalizada evento) {
        reportarResultadoMtc.alFinalizarInspeccion(evento);
    }
}
