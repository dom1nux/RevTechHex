package org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionFixtures.*;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.TipoPrueba.*;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.VeredictoPrueba.*;

/**
 * Verifica el adaptador contra MySQL real (requiere {@code docker compose up -d mysql}).
 * Cada prueba se ejecuta en una transacción que se revierte; {@code validate} garantiza que el
 * mapeo JPA coincide con el esquema existente sin modificarlo.
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=validate")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(InspeccionPersistenceAdapter.class)
class InspeccionPersistenceAdapterTest {

    @Autowired
    private InspeccionPersistenceAdapter adapter;

    @Autowired
    private EntityManager entityManager;

    private InspeccionTecnica guardarYRecargar(InspeccionTecnica inspeccion) {
        InspeccionTecnica guardada = adapter.guardar(inspeccion);
        entityManager.flush();
        entityManager.clear();
        return adapter.buscarPorId(guardada.getId()).orElseThrow();
    }

    private InspeccionTecnica finalizarConPruebas(VehiculoId vehiculo, ResultadoPrueba... pruebas) {
        InspeccionTecnica inspeccion = guardarYRecargar(InspeccionTecnica.registrar(vehiculo, INSPECTOR, SUPERVISOR));
        inspeccion.iniciar(INICIO);
        inspeccion = guardarYRecargar(inspeccion);
        for (ResultadoPrueba prueba : pruebas) {
            inspeccion.registrarPrueba(prueba);
            inspeccion = guardarYRecargar(inspeccion);
        }
        inspeccion.finalizar("Observaciones finales", FIN);
        return guardarYRecargar(inspeccion);
    }

    @Test
    @DisplayName("Recorre el ciclo de vida completo y persiste el certificado en cascada")
    void cicloCompletoConCertificado() {
        InspeccionTecnica finalizada = finalizarConPruebas(VEHICULO, prueba(FRENOS, APROBADO), prueba(LUCES, APROBADO));

        assertThat(finalizada.getId()).isNotNull();
        assertThat(finalizada.getEstado()).isEqualTo(EstadoProceso.FINALIZADA);
        assertThat(finalizada.getResultado()).isEqualTo(ResultadoInspeccion.APTO);
        assertThat(finalizada.getEvaluaciones()).containsExactlyInAnyOrder(prueba(FRENOS, APROBADO), prueba(LUCES, APROBADO));
        assertThat(finalizada.getPeriodo()).isEqualTo(new PeriodoInspeccion(INICIO, FIN));
        assertThat(finalizada.getCertificado().getIdCertificado()).isNotNull();
        assertThat(finalizada.getCertificado().getInspeccionId()).isEqualTo(finalizada.getId());
        assertThat(finalizada.getActa()).isNull();
    }

    @Test
    @DisplayName("Persiste el acta y permite registrar la reinspección de esa inspección")
    void actaYReinspeccion() {
        InspeccionTecnica observada = finalizarConPruebas(VehiculoId.of(2L), prueba(EMISIONES, RECHAZADO));
        assertThat(observada.getResultado()).isEqualTo(ResultadoInspeccion.OBSERVADO);
        assertThat(observada.getActa().getIdActa()).isNotNull();

        InspeccionTecnica reinspeccion = guardarYRecargar(
                InspeccionTecnica.reinspeccionar(observada, VehiculoId.of(2L), INSPECTOR, null));

        assertThat(reinspeccion.getTipo()).isEqualTo(TipoInspeccion.REINSPECCION);
        assertThat(reinspeccion.getOrigen()).isEqualTo(observada.getId());
    }

    @Test
    @DisplayName("Una inspección inexistente devuelve vacío")
    void inexistente() {
        assertThat(adapter.buscarPorId(InspeccionId.of(Long.MAX_VALUE))).isEmpty();
    }
}
