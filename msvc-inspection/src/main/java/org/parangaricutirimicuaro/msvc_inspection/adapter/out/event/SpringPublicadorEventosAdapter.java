package org.parangaricutirimicuaro.msvc_inspection.adapter.out.event;

import org.parangaricutirimicuaro.msvc_inspection.application.port.out.PublicadorEventosPort;
import org.parangaricutirimicuaro.msvc_inspection.domain.event.EventoDominio;
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
