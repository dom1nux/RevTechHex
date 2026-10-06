package org.parangaricutirimicuaro.revtech.adapter.out.client.dto;

public record MtcValidacionResponseDto(
        boolean autorizado,
        String codigoValidacion,
        String mensaje
) {}
