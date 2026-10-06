package org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence.entity.InspeccionTecnicaJpaEntity;
import org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence.entity.PeriodoInspeccionEmbeddable;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionFixtures.*;

class InspeccionPersistenceMapperTest {

    private static InspeccionTecnica idaYVuelta(InspeccionTecnica original) {
        return InspeccionPersistenceMapper.toDomain(InspeccionPersistenceMapper.toEntity(original));
    }

    @Test
    @DisplayName("Una inspección nueva se mapea sin id, periodo, resultado ni documentos")
    void mapeaInspeccionNueva() {
        InspeccionTecnicaJpaEntity entity = InspeccionPersistenceMapper.toEntity(
                InspeccionTecnica.registrar(VEHICULO, INSPECTOR, null));

        assertThat(entity.getIdInspeccion()).isNull();
        assertThat(entity.getIdVehiculo()).isEqualTo(1L);
        assertThat(entity.getEstadoProceso()).isEqualTo(EstadoProceso.REGISTRADA);
        assertThat(entity.getPeriodoInspeccion()).isNull();
        assertThat(entity.getResultadoCondicion()).isNull();
        assertThat(entity.getEvaluaciones()).isEmpty();
        assertThat(entity.getCertificado()).isNull();
        assertThat(entity.getActa()).isNull();
    }

    @Test
    @DisplayName("Las pruebas se almacenan con los nombres canónicos del catálogo")
    void pruebasCanonicas() {
        InspeccionTecnicaJpaEntity entity = InspeccionPersistenceMapper.toEntity(finalizadaObservada());

        assertThat(entity.getEvaluaciones()).singleElement()
                .satisfies(p -> {
                    assertThat(p.getPrueba()).isEqualTo("EMISIONES");
                    assertThat(p.getResultado()).isEqualTo("RECHAZADO");
                });
    }

    @Test
    @DisplayName("Ida y vuelta conserva una inspección observada con su acta")
    void idaYVueltaConActa() {
        InspeccionTecnica original = finalizadaObservada();

        assertThat(idaYVuelta(original)).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    @DisplayName("Ida y vuelta conserva una reinspección en curso")
    void idaYVueltaReinspeccion() {
        InspeccionTecnica original = InspeccionTecnica.reconstituir(new InspeccionTecnica.Snapshot(
                InspeccionId.of(101L), VEHICULO, INSPECTOR, null, TipoInspeccion.REINSPECCION, ID,
                EstadoProceso.EN_PROCESO, null, PeriodoInspeccion.iniciadoEn(INICIO), null,
                List.of(prueba(TipoPrueba.EMISIONES, VeredictoPrueba.APROBADO)), null, null));

        assertThat(idaYVuelta(original)).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    @DisplayName("Un periodo embebido sin fechas (fila heredada) se interpreta como periodo ausente")
    void periodoVacioHeredado() {
        InspeccionTecnicaJpaEntity entity = InspeccionPersistenceMapper.toEntity(registrada());
        entity.setPeriodoInspeccion(new PeriodoInspeccionEmbeddable(null, null));

        assertThat(InspeccionPersistenceMapper.toDomain(entity).getPeriodo()).isNull();
    }

    @Test
    @DisplayName("Una fila inconsistente con el ciclo de vida no se reconstituye")
    void filaInconsistente() {
        InspeccionTecnicaJpaEntity entity = InspeccionPersistenceMapper.toEntity(finalizadaApta());
        entity.setCertificado(null);

        assertThatThrownBy(() -> InspeccionPersistenceMapper.toDomain(entity))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("inconsistente");
    }
}
