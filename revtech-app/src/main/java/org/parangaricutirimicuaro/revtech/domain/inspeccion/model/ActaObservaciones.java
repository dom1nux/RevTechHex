package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import java.time.LocalDateTime;

/**
 * Entidad interna del agregado: documento con las deficiencias a corregir antes de una reinspección.
 * Solo la raíz {@link InspeccionTecnica} puede emitirla.
 */
public final class ActaObservaciones {

    private final Long idActa;
    private final InspeccionId inspeccionId;
    private final String observaciones;
    private final LocalDateTime fechaEmision;

    private ActaObservaciones(Long idActa, InspeccionId inspeccionId, String observaciones, LocalDateTime fechaEmision) {
        this.idActa = idActa;
        this.inspeccionId = inspeccionId;
        this.observaciones = observaciones;
        this.fechaEmision = fechaEmision;
    }

    static ActaObservaciones emitir(InspeccionId inspeccionId, String observaciones, LocalDateTime fechaEmision) {
        return new ActaObservaciones(null, inspeccionId, observaciones, fechaEmision);
    }

    /**
     * Reconstruye un acta ya emitida a partir de su representación persistida.
     */
    public static ActaObservaciones reconstituir(Long idActa, InspeccionId inspeccionId, String observaciones, LocalDateTime fechaEmision) {
        return new ActaObservaciones(idActa, inspeccionId, observaciones, fechaEmision);
    }

    public Long getIdActa() {
        return idActa;
    }

    public InspeccionId getInspeccionId() {
        return inspeccionId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
}
