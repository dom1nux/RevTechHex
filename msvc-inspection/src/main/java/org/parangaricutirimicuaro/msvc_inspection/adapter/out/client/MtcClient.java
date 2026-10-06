package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client;

import org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.dto.MtcValidacionResponseDto;

public interface MtcClient {
    MtcValidacionResponseDto validarNormativa(Long idInspeccion, String placa, String resultado);
}
