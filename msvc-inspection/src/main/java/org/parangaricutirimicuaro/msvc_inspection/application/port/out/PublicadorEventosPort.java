package org.parangaricutirimicuaro.msvc_inspection.application.port.out;

import org.parangaricutirimicuaro.msvc_inspection.domain.event.EventoDominio;

import java.util.List;

/**
 * Difunde los eventos de dominio una vez que el cambio que los originó fue persistido.
 */
public interface PublicadorEventosPort {

    void publicar(List<EventoDominio> eventos);
}
