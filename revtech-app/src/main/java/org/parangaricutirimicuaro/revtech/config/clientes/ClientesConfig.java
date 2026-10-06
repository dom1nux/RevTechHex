package org.parangaricutirimicuaro.revtech.config.clientes;

import org.parangaricutirimicuaro.revtech.application.clientes.port.out.ClienteRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.clientes.port.out.VehiculoRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.clientes.service.ClienteApplicationService;
import org.parangaricutirimicuaro.revtech.application.clientes.service.VehiculoApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ensambla el contexto Atención a Clientes: la capa de aplicación no conoce Spring, por lo que se registra aquí.
 */
@Configuration
public class ClientesConfig {

    @Bean
    ClienteApplicationService clienteApplicationService(ClienteRepositoryPort repository) {
        return new ClienteApplicationService(repository);
    }

    @Bean
    VehiculoApplicationService vehiculoApplicationService(VehiculoRepositoryPort repository) {
        return new VehiculoApplicationService(repository);
    }
}
