package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Inspecciones persistidas y coherentes en cada estado del ciclo de vida, para pruebas de todas las capas.
 */
public final class InspeccionFixtures {

    public static final InspeccionId ID = InspeccionId.of(100L);
    public static final VehiculoId VEHICULO = VehiculoId.of(1L);
    public static final PersonalId INSPECTOR = PersonalId.of(1L);
    public static final PersonalId SUPERVISOR = PersonalId.of(2L);
    public static final LocalDateTime INICIO = LocalDateTime.of(2026, 10, 5, 9, 0);
    public static final LocalDateTime FIN = INICIO.plusHours(1);

    private InspeccionFixtures() {
    }

    public static ResultadoPrueba prueba(TipoPrueba tipo, VeredictoPrueba veredicto) {
        return new ResultadoPrueba(tipo, veredicto);
    }

    public static InspeccionTecnica registrada() {
        return InspeccionTecnica.reconstituir(new InspeccionTecnica.Snapshot(ID, VEHICULO, INSPECTOR, SUPERVISOR,
                TipoInspeccion.INSPECCION, null, EstadoProceso.REGISTRADA, null, null, null, List.of(), null, null));
    }

    public static InspeccionTecnica enProceso(ResultadoPrueba... pruebas) {
        return InspeccionTecnica.reconstituir(new InspeccionTecnica.Snapshot(ID, VEHICULO, INSPECTOR, SUPERVISOR,
                TipoInspeccion.INSPECCION, null, EstadoProceso.EN_PROCESO, null, PeriodoInspeccion.iniciadoEn(INICIO),
                null, List.of(pruebas), null, null));
    }

    public static InspeccionTecnica finalizadaApta() {
        return InspeccionTecnica.reconstituir(new InspeccionTecnica.Snapshot(ID, VEHICULO, INSPECTOR, SUPERVISOR,
                TipoInspeccion.INSPECCION, null, EstadoProceso.FINALIZADA, "Sin observaciones",
                new PeriodoInspeccion(INICIO, FIN), ResultadoInspeccion.APTO,
                List.of(prueba(TipoPrueba.FRENOS, VeredictoPrueba.APROBADO)),
                CertificadoInspeccion.reconstituir(7L, ID, FIN), null));
    }

    public static InspeccionTecnica finalizadaObservada() {
        return InspeccionTecnica.reconstituir(new InspeccionTecnica.Snapshot(ID, VEHICULO, INSPECTOR, SUPERVISOR,
                TipoInspeccion.INSPECCION, null, EstadoProceso.FINALIZADA, "Exceso de emisiones",
                new PeriodoInspeccion(INICIO, FIN), ResultadoInspeccion.OBSERVADO,
                List.of(prueba(TipoPrueba.EMISIONES, VeredictoPrueba.RECHAZADO)),
                null, ActaObservaciones.reconstituir(9L, ID, "Exceso de emisiones", FIN)));
    }
}
