package org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity.ActaObservacionesJpaEntity;
import org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity.CertificadoInspeccionJpaEntity;
import org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity.InspeccionTecnicaJpaEntity;
import org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity.PeriodoInspeccionEmbeddable;
import org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity.ResultadoPruebaEmbeddable;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.*;

import java.util.ArrayList;

/**
 * Traduce el agregado de dominio a su representación JPA y viceversa a través de su {@link InspeccionTecnica.Snapshot}.
 */
final class InspeccionPersistenceMapper {

    private InspeccionPersistenceMapper() {
    }

    static InspeccionTecnicaJpaEntity toEntity(InspeccionTecnica inspeccion) {
        InspeccionTecnica.Snapshot s = inspeccion.snapshot();
        InspeccionTecnicaJpaEntity entity = new InspeccionTecnicaJpaEntity();
        entity.setIdInspeccion(valor(s.id()));
        entity.setIdVehiculo(s.vehiculo().valor());
        entity.setIdInspector(s.inspector().valor());
        entity.setIdSupervisor(valor(s.supervisor()));
        entity.setTipoInspeccion(s.tipo());
        entity.setIdInspeccionOrigen(valor(s.origen()));
        entity.setEstadoProceso(s.estado());
        entity.setObservaciones(s.observaciones());

        if (s.periodo() != null) {
            entity.setPeriodoInspeccion(new PeriodoInspeccionEmbeddable(s.periodo().fechaInicio(), s.periodo().fechaFin()));
        }
        if (s.resultado() != null) {
            entity.setResultadoCondicion(s.resultado().condicion());
        }

        entity.setEvaluaciones(new ArrayList<>(s.evaluaciones().stream()
                .map(p -> new ResultadoPruebaEmbeddable(p.prueba().name(), p.resultado().name()))
                .toList()));

        CertificadoInspeccion certificado = s.certificado();
        if (certificado != null) {
            CertificadoInspeccionJpaEntity certificadoEntity = new CertificadoInspeccionJpaEntity();
            certificadoEntity.setIdCertificado(certificado.getIdCertificado());
            certificadoEntity.setInspeccionId(valor(certificado.getInspeccionId()));
            certificadoEntity.setFechaEmision(certificado.getFechaEmision());
            entity.setCertificado(certificadoEntity);
        }

        ActaObservaciones acta = s.acta();
        if (acta != null) {
            ActaObservacionesJpaEntity actaEntity = new ActaObservacionesJpaEntity();
            actaEntity.setIdActa(acta.getIdActa());
            actaEntity.setInspeccionId(valor(acta.getInspeccionId()));
            actaEntity.setObservaciones(acta.getObservaciones());
            actaEntity.setFechaEmision(acta.getFechaEmision());
            entity.setActa(actaEntity);
        }
        return entity;
    }

    static InspeccionTecnica toDomain(InspeccionTecnicaJpaEntity entity) {
        PeriodoInspeccionEmbeddable periodoEntity = entity.getPeriodoInspeccion();
        PeriodoInspeccion periodo = periodoEntity != null && periodoEntity.getFechaInicio() != null
                ? new PeriodoInspeccion(periodoEntity.getFechaInicio(), periodoEntity.getFechaFin())
                : null;

        ResultadoInspeccion resultado = entity.getResultadoCondicion() != null
                ? new ResultadoInspeccion(entity.getResultadoCondicion())
                : null;

        CertificadoInspeccionJpaEntity c = entity.getCertificado();
        CertificadoInspeccion certificado = c != null
                ? CertificadoInspeccion.reconstituir(c.getIdCertificado(), InspeccionId.ofNullable(c.getInspeccionId()), c.getFechaEmision())
                : null;

        ActaObservacionesJpaEntity a = entity.getActa();
        ActaObservaciones acta = a != null
                ? ActaObservaciones.reconstituir(a.getIdActa(), InspeccionId.ofNullable(a.getInspeccionId()),
                a.getObservaciones(), a.getFechaEmision())
                : null;

        return InspeccionTecnica.reconstituir(new InspeccionTecnica.Snapshot(
                InspeccionId.of(entity.getIdInspeccion()),
                VehiculoId.of(entity.getIdVehiculo()),
                PersonalId.of(entity.getIdInspector()),
                PersonalId.ofNullable(entity.getIdSupervisor()),
                entity.getTipoInspeccion(),
                InspeccionId.ofNullable(entity.getIdInspeccionOrigen()),
                entity.getEstadoProceso(),
                entity.getObservaciones(),
                periodo,
                resultado,
                entity.getEvaluaciones().stream()
                        .map(p -> ResultadoPrueba.de(p.getPrueba(), p.getResultado()))
                        .toList(),
                certificado,
                acta));
    }

    private static Long valor(InspeccionId id) {
        return id != null ? id.valor() : null;
    }

    private static Long valor(PersonalId id) {
        return id != null ? id.valor() : null;
    }
}
