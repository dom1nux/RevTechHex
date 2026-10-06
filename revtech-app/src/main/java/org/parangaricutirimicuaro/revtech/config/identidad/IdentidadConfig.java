package org.parangaricutirimicuaro.revtech.config.identidad;

import org.parangaricutirimicuaro.revtech.application.identidad.port.out.RolAccesoRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.UsuarioRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.identidad.service.RolApplicationService;
import org.parangaricutirimicuaro.revtech.application.identidad.service.UsuarioApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ensambla el contexto Identidad y Acceso: la capa de aplicación no conoce Spring, por lo que se registra aquí.
 */
@Configuration
public class IdentidadConfig {

    @Bean
    UsuarioApplicationService usuarioApplicationService(UsuarioRepositoryPort usuarios, RolAccesoRepositoryPort roles) {
        return new UsuarioApplicationService(usuarios, roles);
    }

    @Bean
    RolApplicationService rolApplicationService(RolAccesoRepositoryPort roles) {
        return new RolApplicationService(roles);
    }
}
