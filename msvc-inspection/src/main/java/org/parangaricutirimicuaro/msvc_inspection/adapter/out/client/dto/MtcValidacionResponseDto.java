package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.dto;

public record MtcValidacionResponseDto(
        boolean autorizado,
        String codigoValidacion,
        String mensaje
) {}
