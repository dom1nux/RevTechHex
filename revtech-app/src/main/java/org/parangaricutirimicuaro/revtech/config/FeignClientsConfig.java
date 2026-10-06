package org.parangaricutirimicuaro.revtech.config;

import org.parangaricutirimicuaro.revtech.adapter.out.client.feign.FeignMtcClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita los clientes OpenFeign (solo el del MTC).
 */
@Configuration
@EnableFeignClients(basePackageClasses = FeignMtcClient.class)
public class FeignClientsConfig {
}
