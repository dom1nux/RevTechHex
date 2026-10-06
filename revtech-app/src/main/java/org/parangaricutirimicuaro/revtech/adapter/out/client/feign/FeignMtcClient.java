package org.parangaricutirimicuaro.revtech.adapter.out.client.feign;

import org.parangaricutirimicuaro.revtech.adapter.out.client.MtcClient;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.MtcValidacionResponseDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente HTTP real del MTC, generado por OpenFeign. Activo cuando {@code revtech.clients.mock=false}.
 */
@FeignClient(name = "mtc-service", url = "${clients.mtc.url:http://localhost:8089}")
@ConditionalOnProperty(name = "revtech.clients.mock", havingValue = "false")
public interface FeignMtcClient extends MtcClient {

    @Override
    @PostMapping("/api/mtc/validar")
    MtcValidacionResponseDto validarNormativa(
            @RequestParam("idInspeccion") Long idInspeccion,
            @RequestParam("placa") String placa,
            @RequestParam("resultado") String resultado
    );
}
