package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.event.InspeccionFinalizada;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.ReglaNegocioVioladaException;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.TransicionEstadoInvalidaException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionFixtures.*;
import static org.parangaricutirimicuaro.revtech.domain.inspeccion.model.TipoPrueba.*;
import static org.parangaricutirimicuaro.revtech.domain.inspeccion.model.VeredictoPrueba.*;

class InspeccionTecnicaTest {

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("Una inspección nueva queda REGISTRADA, sin periodo, pruebas ni documentos")
        void registraInspeccionNueva() {
            InspeccionTecnica inspeccion = InspeccionTecnica.registrar(VEHICULO, INSPECTOR, SUPERVISOR);

            assertThat(inspeccion.getId()).isNull();
            assertThat(inspeccion.getEstado()).isEqualTo(EstadoProceso.REGISTRADA);
            assertThat(inspeccion.getTipo()).isEqualTo(TipoInspeccion.INSPECCION);
            assertThat(inspeccion.getOrigen()).isNull();
            assertThat(inspeccion.getPeriodo()).isNull();
            assertThat(inspeccion.getEvaluaciones()).isEmpty();
            assertThat(inspeccion.getResultado()).isNull();
            assertThat(inspeccion.getCertificado()).isNull();
            assertThat(inspeccion.getActa()).isNull();
            assertThat(inspeccion.extraerEventos()).isEmpty();
        }

        @Test
        @DisplayName("Vehículo e inspector son obligatorios; el supervisor es opcional")
        void datosObligatorios() {
            assertThatThrownBy(() -> InspeccionTecnica.registrar(null, INSPECTOR, null))
                    .isInstanceOf(DatoInvalidoException.class).hasMessageContaining("vehículo");
            assertThatThrownBy(() -> InspeccionTecnica.registrar(VEHICULO, null, null))
                    .isInstanceOf(DatoInvalidoException.class).hasMessageContaining("inspector");
            assertThat(InspeccionTecnica.registrar(VEHICULO, INSPECTOR, null).getSupervisor()).isNull();
        }
    }

    @Nested
    @DisplayName("Reinspección")
    class Reinspeccion {

        @Test
        @DisplayName("Se registra a partir de una inspección OBSERVADA del mismo vehículo")
        void reinspeccionaObservada() {
            InspeccionTecnica reinspeccion = InspeccionTecnica.reinspeccionar(
                    finalizadaObservada(), VEHICULO, PersonalId.of(3L), null);

            assertThat(reinspeccion.getTipo()).isEqualTo(TipoInspeccion.REINSPECCION);
            assertThat(reinspeccion.getOrigen()).isEqualTo(ID);
            assertThat(reinspeccion.getVehiculo()).isEqualTo(VEHICULO);
            assertThat(reinspeccion.getInspector()).isEqualTo(PersonalId.of(3L));
            assertThat(reinspeccion.getEstado()).isEqualTo(EstadoProceso.REGISTRADA);
        }

        @Test
        @DisplayName("No procede si la inspección de origen resultó APTA")
        void noReinspeccionaApta() {
            assertThatThrownBy(() -> InspeccionTecnica.reinspeccionar(finalizadaApta(), VEHICULO, INSPECTOR, null))
                    .isInstanceOf(ReglaNegocioVioladaException.class)
                    .hasMessageContaining("OBSERVADO")
                    .hasMessageContaining("APTO");
        }

        @Test
        @DisplayName("No procede si la inspección de origen aún no finaliza")
        void noReinspeccionaSinFinalizar() {
            assertThatThrownBy(() -> InspeccionTecnica.reinspeccionar(enProceso(), VEHICULO, INSPECTOR, null))
                    .isInstanceOf(ReglaNegocioVioladaException.class)
                    .hasMessageContaining("EN_PROCESO");
        }

        @Test
        @DisplayName("Debe realizarse al mismo vehículo de la inspección de origen")
        void mismoVehiculo() {
            assertThatThrownBy(() -> InspeccionTecnica.reinspeccionar(
                    finalizadaObservada(), VehiculoId.of(2L), INSPECTOR, null))
                    .isInstanceOf(ReglaNegocioVioladaException.class)
                    .hasMessageContaining("mismo vehículo");
        }

        @Test
        @DisplayName("Exige una inspección de origen ya persistida")
        void origenObligatorio() {
            InspeccionTecnica sinPersistir = InspeccionTecnica.registrar(VEHICULO, INSPECTOR, null);

            assertThatThrownBy(() -> InspeccionTecnica.reinspeccionar(null, VEHICULO, INSPECTOR, null))
                    .isInstanceOf(DatoInvalidoException.class);
            assertThatThrownBy(() -> InspeccionTecnica.reinspeccionar(sinPersistir, VEHICULO, INSPECTOR, null))
                    .isInstanceOf(DatoInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("Inicio")
    class Inicio {

        @Test
        @DisplayName("Iniciar pasa a EN_PROCESO y fija la fecha de inicio")
        void iniciaInspeccionRegistrada() {
            InspeccionTecnica inspeccion = registrada();

            inspeccion.iniciar(INICIO);

            assertThat(inspeccion.getEstado()).isEqualTo(EstadoProceso.EN_PROCESO);
            assertThat(inspeccion.getPeriodo()).isEqualTo(PeriodoInspeccion.iniciadoEn(INICIO));
        }

        @Test
        @DisplayName("No se puede iniciar una inspección EN_PROCESO ni una FINALIZADA")
        void soloDesdeRegistrada() {
            assertThatThrownBy(() -> enProceso().iniciar(FIN))
                    .isInstanceOf(TransicionEstadoInvalidaException.class);
            assertThatThrownBy(() -> finalizadaApta().iniciar(FIN))
                    .isInstanceOf(TransicionEstadoInvalidaException.class)
                    .hasMessageContaining("FINALIZADA");
        }
    }

    @Nested
    @DisplayName("Registro de pruebas")
    class RegistroPruebas {

        @Test
        @DisplayName("Acumula las pruebas registradas durante el proceso")
        void acumulaPruebas() {
            InspeccionTecnica inspeccion = enProceso();

            inspeccion.registrarPrueba(prueba(FRENOS, APROBADO));
            inspeccion.registrarPrueba(prueba(LUCES, OBSERVADO));

            assertThat(inspeccion.getEvaluaciones()).containsExactly(prueba(FRENOS, APROBADO), prueba(LUCES, OBSERVADO));
        }

        @Test
        @DisplayName("No admite registrar dos veces la misma prueba")
        void rechazaPruebaDuplicada() {
            InspeccionTecnica inspeccion = enProceso(prueba(FRENOS, APROBADO));

            assertThatThrownBy(() -> inspeccion.registrarPrueba(prueba(FRENOS, RECHAZADO)))
                    .isInstanceOf(ReglaNegocioVioladaException.class)
                    .hasMessageContaining("FRENOS");
            assertThat(inspeccion.getEvaluaciones()).containsExactly(prueba(FRENOS, APROBADO));
        }

        @Test
        @DisplayName("No admite pruebas si la inspección no está EN_PROCESO")
        void rechazaPruebasFueraDeProceso() {
            assertThatThrownBy(() -> registrada().registrarPrueba(prueba(FRENOS, APROBADO)))
                    .isInstanceOf(TransicionEstadoInvalidaException.class);
            assertThatThrownBy(() -> finalizadaApta().registrarPrueba(prueba(LUCES, APROBADO)))
                    .isInstanceOf(TransicionEstadoInvalidaException.class);
        }

        @Test
        @DisplayName("La colección expuesta es inmutable: solo la raíz puede modificarla")
        void evaluacionesInmutables() {
            InspeccionTecnica inspeccion = enProceso(prueba(FRENOS, APROBADO));

            assertThatThrownBy(() -> inspeccion.getEvaluaciones().add(prueba(LUCES, APROBADO)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Finalización")
    class Finalizacion {

        @Test
        @DisplayName("Con todas las pruebas aprobadas emite certificado y el resultado es APTO")
        void emiteCertificado() {
            InspeccionTecnica inspeccion = enProceso(prueba(FRENOS, APROBADO), prueba(EMISIONES, APROBADO));

            inspeccion.finalizar("Vehículo en perfectas condiciones", FIN);

            assertThat(inspeccion.getEstado()).isEqualTo(EstadoProceso.FINALIZADA);
            assertThat(inspeccion.getResultado()).isEqualTo(ResultadoInspeccion.APTO);
            assertThat(inspeccion.getActa()).isNull();
            assertThat(inspeccion.getCertificado().getInspeccionId()).isEqualTo(ID);
            assertThat(inspeccion.getCertificado().getFechaEmision()).isEqualTo(FIN);
            assertThat(inspeccion.getObservaciones()).isEqualTo("Vehículo en perfectas condiciones");
            assertThat(inspeccion.getPeriodo()).isEqualTo(new PeriodoInspeccion(INICIO, FIN));
        }

        @Test
        @DisplayName("Con una prueba RECHAZADA u OBSERVADA emite acta y el resultado es OBSERVADO")
        void emiteActa() {
            InspeccionTecnica inspeccion = enProceso(prueba(FRENOS, APROBADO), prueba(EMISIONES, RECHAZADO));

            inspeccion.finalizar("Exceso de emisiones de gases", FIN);

            assertThat(inspeccion.getResultado()).isEqualTo(ResultadoInspeccion.OBSERVADO);
            assertThat(inspeccion.getCertificado()).isNull();
            assertThat(inspeccion.getActa().getObservaciones()).isEqualTo("Exceso de emisiones de gases");
            assertThat(inspeccion.getActa().getInspeccionId()).isEqualTo(ID);
        }

        @Test
        @DisplayName("Sin observaciones del inspector, el acta usa un texto por defecto")
        void actaConObservacionPorDefecto() {
            InspeccionTecnica inspeccion = enProceso(prueba(LUCES, OBSERVADO));

            inspeccion.finalizar(null, FIN);

            assertThat(inspeccion.getActa().getObservaciones()).isEqualTo(InspeccionTecnica.OBSERVACION_POR_DEFECTO);
            assertThat(inspeccion.getObservaciones()).isNull();
        }

        @Test
        @DisplayName("Registra el evento InspeccionFinalizada con la condición obtenida")
        void registraEvento() {
            InspeccionTecnica inspeccion = enProceso(prueba(EMISIONES, RECHAZADO));

            inspeccion.finalizar(null, FIN);

            assertThat(inspeccion.extraerEventos()).containsExactly(
                    new InspeccionFinalizada(ID, VEHICULO, CondicionInspeccion.OBSERVADO, FIN));
            assertThat(inspeccion.extraerEventos()).as("extraer vacía la cola de eventos").isEmpty();
        }

        @Test
        @DisplayName("No se puede finalizar sin pruebas registradas y no se registra ningún evento")
        void noFinalizaSinPruebas() {
            InspeccionTecnica inspeccion = enProceso();

            assertThatThrownBy(() -> inspeccion.finalizar(null, FIN))
                    .isInstanceOf(TransicionEstadoInvalidaException.class)
                    .hasMessageContaining("sin haber registrado pruebas");
            assertThat(inspeccion.getEstado()).isEqualTo(EstadoProceso.EN_PROCESO);
            assertThat(inspeccion.extraerEventos()).isEmpty();
        }

        @Test
        @DisplayName("No se puede finalizar una inspección que no está EN_PROCESO")
        void noFinalizaFueraDeProceso() {
            assertThatThrownBy(() -> registrada().finalizar(null, FIN))
                    .isInstanceOf(TransicionEstadoInvalidaException.class);
            assertThatThrownBy(() -> finalizadaApta().finalizar(null, FIN))
                    .isInstanceOf(TransicionEstadoInvalidaException.class);
        }
    }

    @Nested
    @DisplayName("Reconstitución")
    class Reconstitucion {

        private InspeccionTecnica.Snapshot base(EstadoProceso estado, PeriodoInspeccion periodo, ResultadoInspeccion resultado,
                                                List<ResultadoPrueba> pruebas, CertificadoInspeccion certificado,
                                                ActaObservaciones acta) {
            return new InspeccionTecnica.Snapshot(ID, VEHICULO, INSPECTOR, null, TipoInspeccion.INSPECCION, null,
                    estado, null, periodo, resultado, pruebas, certificado, acta);
        }

        @Test
        @DisplayName("El snapshot de un agregado lo reconstituye sin pérdida")
        void idaYVuelta() {
            InspeccionTecnica original = finalizadaObservada();

            assertThat(InspeccionTecnica.reconstituir(original.snapshot()))
                    .usingRecursiveComparison().ignoringFields("eventos").isEqualTo(original);
        }

        @Test
        @DisplayName("Rechaza una inspección FINALIZADA APTA que no tiene certificado")
        void finalizadaSinDocumento() {
            assertThatThrownBy(() -> base(EstadoProceso.FINALIZADA, new PeriodoInspeccion(INICIO, FIN),
                    ResultadoInspeccion.APTO, List.of(prueba(FRENOS, APROBADO)), null, null))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("FINALIZADA");
        }

        @Test
        @DisplayName("Rechaza una inspección REGISTRADA que ya tiene pruebas")
        void registradaConPruebas() {
            assertThatThrownBy(() -> base(EstadoProceso.REGISTRADA, null, null,
                    List.of(prueba(FRENOS, APROBADO)), null, null))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("REGISTRADA");
        }

        @Test
        @DisplayName("Rechaza una inspección EN_PROCESO con periodo ya cerrado")
        void enProcesoCerrada() {
            assertThatThrownBy(() -> base(EstadoProceso.EN_PROCESO, new PeriodoInspeccion(INICIO, FIN),
                    null, List.of(), null, null))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("Rechaza una reinspección sin origen")
        void reinspeccionSinOrigen() {
            assertThatThrownBy(() -> new InspeccionTecnica.Snapshot(ID, VEHICULO, INSPECTOR, null,
                    TipoInspeccion.REINSPECCION, null, EstadoProceso.REGISTRADA, null, null, null, List.of(), null, null))
                    .isInstanceOf(DatoInvalidoException.class);
        }

        @Test
        @DisplayName("Solo se reconstituyen inspecciones con identificador")
        void exigeIdentificador() {
            InspeccionTecnica.Snapshot sinId = InspeccionTecnica.registrar(VEHICULO, INSPECTOR, null).snapshot();

            assertThatThrownBy(() -> InspeccionTecnica.reconstituir(sinId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
