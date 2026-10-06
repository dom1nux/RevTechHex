package org.parangaricutirimicuaro.revtech.application.inspeccion.port.out;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.event.EventoDominio;

import java.util.List;

/**
 * Difunde los eventos de dominio una vez que el cambio que los originó fue persistido.
 */
public interface PublicadorEventosPort {

    void publicar(List<EventoDominio> eventos);
}
