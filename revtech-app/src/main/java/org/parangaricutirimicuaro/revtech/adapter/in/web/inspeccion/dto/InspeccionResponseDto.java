package org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Representación pública de una inspección técnica. Conserva la forma JSON expuesta históricamente por la API.
 */
public record InspeccionResponseDto(
        Long idInspeccion,
        Long idVehiculo,
        Long idInspector,
        Long idSupervisor,
        String tipoInspeccion,
        Long idInspeccionOrigen,
        String estadoProceso,
        String observaciones,
        Periodo periodoInspeccion,
        Resultado resultado,
        List<Prueba> evaluaciones,
        Certificado certificado,
        Acta acta
) {

    public record Periodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {}

    public record Resultado(String condicion) {}

    public record Prueba(String prueba, String resultado) {}

    public record Certificado(Long idCertificado, Long inspeccionId, LocalDateTime fechaEmision) {}

    public record Acta(Long idActa, Long inspeccionId, String observaciones, LocalDateTime fechaEmision) {}
}
