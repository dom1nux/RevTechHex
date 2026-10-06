package org.parangaricutirimicuaro.msvc_inspection.domain.model;

import java.time.LocalDateTime;

/**
 * Entidad interna del agregado: constancia de aprobación emitida cuando el vehículo resulta APTO.
 * Solo la raíz {@link InspeccionTecnica} puede emitirla.
 */
public final class CertificadoInspeccion {

    private final Long idCertificado;
    private final InspeccionId inspeccionId;
    private final LocalDateTime fechaEmision;

    private CertificadoInspeccion(Long idCertificado, InspeccionId inspeccionId, LocalDateTime fechaEmision) {
        this.idCertificado = idCertificado;
        this.inspeccionId = inspeccionId;
        this.fechaEmision = fechaEmision;
    }

    static CertificadoInspeccion emitir(InspeccionId inspeccionId, LocalDateTime fechaEmision) {
        return new CertificadoInspeccion(null, inspeccionId, fechaEmision);
    }

    /**
     * Reconstruye un certificado ya emitido a partir de su representación persistida.
     */
    public static CertificadoInspeccion reconstituir(Long idCertificado, InspeccionId inspeccionId, LocalDateTime fechaEmision) {
        return new CertificadoInspeccion(idCertificado, inspeccionId, fechaEmision);
    }

    public Long getIdCertificado() {
        return idCertificado;
    }

    public InspeccionId getInspeccionId() {
        return inspeccionId;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
}
