package org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion;

import org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion.dto.InspeccionResponseDto;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.ActaObservaciones;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CertificadoInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PeriodoInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PersonalId;

/**
 * Convierte el agregado {@code InspeccionTecnica} en el JSON de respuesta, para que el dominio no dependa del formato web.
 */
final class InspeccionWebMapper {

    private InspeccionWebMapper() {
    }

    static InspeccionResponseDto toResponse(InspeccionTecnica inspeccion) {
        PeriodoInspeccion periodo = inspeccion.getPeriodo();
        CertificadoInspeccion certificado = inspeccion.getCertificado();
        ActaObservaciones acta = inspeccion.getActa();

        return new InspeccionResponseDto(
                valor(inspeccion.getId()),
                inspeccion.getVehiculo().valor(),
                inspeccion.getInspector().valor(),
                valor(inspeccion.getSupervisor()),
                inspeccion.getTipo().name(),
                valor(inspeccion.getOrigen()),
                inspeccion.getEstado().name(),
                inspeccion.getObservaciones(),
                periodo != null
                        ? new InspeccionResponseDto.Periodo(periodo.fechaInicio(), periodo.fechaFin())
                        : new InspeccionResponseDto.Periodo(null, null),
                inspeccion.getResultado() != null
                        ? new InspeccionResponseDto.Resultado(inspeccion.getResultado().condicion().name())
                        : null,
                inspeccion.getEvaluaciones().stream()
                        .map(p -> new InspeccionResponseDto.Prueba(p.prueba().name(), p.resultado().name()))
                        .toList(),
                certificado != null
                        ? new InspeccionResponseDto.Certificado(certificado.getIdCertificado(),
                        valor(certificado.getInspeccionId()), certificado.getFechaEmision())
                        : null,
                acta != null
                        ? new InspeccionResponseDto.Acta(acta.getIdActa(), valor(acta.getInspeccionId()),
                        acta.getObservaciones(), acta.getFechaEmision())
                        : null);
    }

    private static Long valor(InspeccionId id) {
        return id != null ? id.valor() : null;
    }

    private static Long valor(PersonalId id) {
        return id != null ? id.valor() : null;
    }
}
