package org.parangaricutirimicuaro.msvc_inspection.config;

import org.parangaricutirimicuaro.msvc_inspection.application.port.out.InspeccionRepositoryPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.MtcPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.PublicadorEventosPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.UsuarioPort;
import org.parangaricutirimicuaro.msvc_inspection.application.port.out.VehiculoPort;
import org.parangaricutirimicuaro.msvc_inspection.application.service.InspeccionApplicationService;
import org.parangaricutirimicuaro.msvc_inspection.application.service.ReportarResultadoMtcService;
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
