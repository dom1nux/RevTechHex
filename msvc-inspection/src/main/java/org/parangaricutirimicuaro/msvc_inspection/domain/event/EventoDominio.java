package org.parangaricutirimicuaro.msvc_inspection.domain.event;

import java.time.LocalDateTime;

/**
 * Hecho relevante del negocio que ya ocurrió. Los agregados los registran; la aplicación los publica tras persistir.
 */
public interface EventoDominio {

    LocalDateTime ocurridoEn();
}
