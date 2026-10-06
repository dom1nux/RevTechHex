package org.parangaricutirimicuaro.revtech.adapter.out.event.inspeccion;

import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.PublicadorEventosPort;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.event.EventoDominio;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Publica los eventos de dominio en el bus de eventos de Spring (en proceso, síncrono).
 */
@Component
public class SpringPublicadorEventosAdapter implements PublicadorEventosPort {

    private final ApplicationEventPublisher publisher;

    public SpringPublicadorEventosAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publicar(List<EventoDominio> eventos) {
        eventos.forEach(publisher::publishEvent);
    }
}
