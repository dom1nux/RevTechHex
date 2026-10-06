package org.parangaricutirimicuaro.revtech.config.inspeccion;

import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.InspeccionRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.MtcPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.PublicadorEventosPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.UsuarioPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.VehiculoPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.service.InspeccionApplicationService;
import org.parangaricutirimicuaro.revtech.application.inspeccion.service.ReportarResultadoMtcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Ensambla el núcleo hexagonal: la capa de aplicación no conoce Spring, por lo que se registra aquí.
 */
@Configuration
public class InspeccionConfig {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    InspeccionApplicationService inspeccionApplicationService(InspeccionRepositoryPort repository,
                                                              VehiculoPort vehiculoPort,
                                                              UsuarioPort usuarioPort,
                                                              PublicadorEventosPort publicador,
                                                              Clock clock) {
        return new InspeccionApplicationService(repository, vehiculoPort, usuarioPort, publicador, clock);
    }

    @Bean
    ReportarResultadoMtcService reportarResultadoMtcService(VehiculoPort vehiculoPort, MtcPort mtcPort) {
        return new ReportarResultadoMtcService(vehiculoPort, mtcPort);
    }
}
