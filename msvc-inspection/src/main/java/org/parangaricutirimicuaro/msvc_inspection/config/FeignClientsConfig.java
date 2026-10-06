package org.parangaricutirimicuaro.msvc_inspection.config;

import org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.feign.FeignMtcClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = FeignMtcClient.class)
public class FeignClientsConfig {
}
