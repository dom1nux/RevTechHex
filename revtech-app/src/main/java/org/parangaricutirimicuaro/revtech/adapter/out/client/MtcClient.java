package org.parangaricutirimicuaro.revtech.adapter.out.client;

import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.MtcValidacionResponseDto;

public interface MtcClient {
    MtcValidacionResponseDto validarNormativa(Long idInspeccion, String placa, String resultado);
}
