package org.parangaricutirimicuaro.msvc_inspection.adapter.in.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.ReportarResultadoMtcUseCase;
import org.parangaricutirimicuaro.msvc_inspection.domain.event.InspeccionFinalizada;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.CondicionInspeccion;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionFixtures.*;

class InspeccionEventListenerTest {

    @Test
    @DisplayName("Un InspeccionFinalizada publicado en Spring llega al caso de uso de reporte al MTC")
    void enrutaEvento() {
        ReportarResultadoMtcUseCase useCase = mock(ReportarResultadoMtcUseCase.class);
        var evento = new InspeccionFinalizada(ID, VEHICULO, CondicionInspeccion.APTO, FIN);

        try (var context = new AnnotationConfigApplicationContext()) {
            context.registerBean(ReportarResultadoMtcUseCase.class, () -> useCase);
            context.register(InspeccionEventListener.class);
            context.refresh();

            context.publishEvent(evento);
        }

        verify(useCase).alFinalizarInspeccion(evento);
    }
}
