package org.parangaricutirimicuaro.msvc_inspection.adapter.out.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.msvc_inspection.domain.event.InspeccionFinalizada;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.CondicionInspeccion;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionFixtures.*;

class SpringPublicadorEventosAdapterTest {

    @Test
    @DisplayName("Publica cada evento de dominio en el bus de Spring, en orden")
    void publicaCadaEvento() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        var primero = new InspeccionFinalizada(ID, VEHICULO, CondicionInspeccion.APTO, INICIO);
        var segundo = new InspeccionFinalizada(ID, VEHICULO, CondicionInspeccion.OBSERVADO, FIN);

        new SpringPublicadorEventosAdapter(publisher).publicar(List.of(primero, segundo));

        var orden = inOrder(publisher);
        orden.verify(publisher).publishEvent(primero);
        orden.verify(publisher).publishEvent(segundo);
    }

    @Test
    @DisplayName("Sin eventos no publica nada")
    void sinEventos() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        new SpringPublicadorEventosAdapter(publisher).publicar(List.of());

        verifyNoInteractions(publisher);
    }
}
