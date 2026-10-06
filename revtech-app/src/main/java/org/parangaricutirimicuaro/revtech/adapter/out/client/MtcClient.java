package org.parangaricutirimicuaro.revtech.adapter.out.client;

import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.MtcValidacionResponseDto;

/**
 * Cliente del sistema externo MTC. Tiene dos implementaciones: {@code FeignMtcClient} (HTTP real) y {@code MockMtcClient} (simulado).
 */
public interface MtcClient {
    MtcValidacionResponseDto validarNormativa(Long idInspeccion, String placa, String resultado);
}
