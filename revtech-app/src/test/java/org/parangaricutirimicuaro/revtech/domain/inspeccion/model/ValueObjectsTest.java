package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueObjectsTest {

    @Nested
    @DisplayName("TipoPrueba")
    class TipoPruebaTest {

        @ParameterizedTest
        @CsvSource({"Frenos,FRENOS", "dirección,DIRECCION", " Suspensión ,SUSPENSION", "neumáticos,NEUMATICOS", "LUCES,LUCES"})
        @DisplayName("Interpreta el nombre sin distinguir mayúsculas, tildes ni espacios")
        void interpreta(String valor, TipoPrueba esperado) {
            assertThat(TipoPrueba.desde(valor)).isEqualTo(esperado);
        }

        @Test
        @DisplayName("Rechaza pruebas fuera del catálogo de RevTech")
        void desconocida() {
            assertThatThrownBy(() -> TipoPrueba.desde("Tapicería"))
                    .isInstanceOf(DatoInvalidoException.class)
                    .hasMessageContaining("Tapicería");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = "   ")
        @DisplayName("El tipo de prueba es obligatorio")
        void obligatorio(String valor) {
            assertThatThrownBy(() -> TipoPrueba.desde(valor)).isInstanceOf(DatoInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("VeredictoPrueba")
    class VeredictoPruebaTest {

        @ParameterizedTest
        @EnumSource(value = VeredictoPrueba.class, names = {"OBSERVADO", "RECHAZADO"})
        @DisplayName("OBSERVADO y RECHAZADO son deficiencias")
        void deficientes(VeredictoPrueba veredicto) {
            assertThat(veredicto.esDeficiente()).isTrue();
        }

        @Test
        @DisplayName("APROBADO no es deficiencia")
        void aprobado() {
            assertThat(VeredictoPrueba.APROBADO.esDeficiente()).isFalse();
        }

        @Test
        @DisplayName("Rechaza veredictos desconocidos")
        void desconocido() {
            assertThatThrownBy(() -> VeredictoPrueba.desde("CONFORME")).isInstanceOf(DatoInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("ResultadoPrueba")
    class ResultadoPruebaTest {

        @Test
        @DisplayName("Se construye desde texto y es deficiente según su veredicto")
        void desdeTexto() {
            ResultadoPrueba prueba = ResultadoPrueba.de("Emisiones", "rechazado");

            assertThat(prueba).isEqualTo(new ResultadoPrueba(TipoPrueba.EMISIONES, VeredictoPrueba.RECHAZADO));
            assertThat(prueba.esDeficiente()).isTrue();
        }

        @Test
        @DisplayName("Prueba y veredicto son obligatorios")
        void obligatorios() {
            assertThatThrownBy(() -> new ResultadoPrueba(null, VeredictoPrueba.APROBADO)).isInstanceOf(DatoInvalidoException.class);
            assertThatThrownBy(() -> new ResultadoPrueba(TipoPrueba.FRENOS, null)).isInstanceOf(DatoInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("Identificadores tipados")
    class Identificadores {

        @ParameterizedTest
        @ValueSource(longs = {0L, -5L})
        @DisplayName("Solo admiten valores positivos")
        void positivos(long valor) {
            assertThatThrownBy(() -> InspeccionId.of(valor)).isInstanceOf(DatoInvalidoException.class);
            assertThatThrownBy(() -> VehiculoId.of(valor)).isInstanceOf(DatoInvalidoException.class);
            assertThatThrownBy(() -> PersonalId.of(valor)).isInstanceOf(DatoInvalidoException.class);
        }

        @Test
        @DisplayName("of exige valor; ofNullable representa una referencia ausente")
        void nulos() {
            assertThatThrownBy(() -> VehiculoId.of(null)).isInstanceOf(DatoInvalidoException.class);
            assertThat(PersonalId.ofNullable(null)).isNull();
            assertThat(PersonalId.ofNullable(2L)).isEqualTo(PersonalId.of(2L));
        }

        @Test
        @DisplayName("Identificadores con el mismo valor pero distinto concepto no son iguales")
        void noIntercambiables() {
            assertThat((Object) VehiculoId.of(1L)).isNotEqualTo(PersonalId.of(1L));
        }
    }

    @Nested
    @DisplayName("PeriodoInspeccion")
    class PeriodoInspeccionTest {

        private final LocalDateTime inicio = LocalDateTime.of(2026, 10, 5, 9, 0);

        @Test
        @DisplayName("Se abre con fecha de inicio y se cierra produciendo una nueva instancia")
        void abreYCierra() {
            PeriodoInspeccion abierto = PeriodoInspeccion.iniciadoEn(inicio);
            PeriodoInspeccion cerrado = abierto.finalizadoEn(inicio.plusHours(1));

            assertThat(abierto.fechaFin()).isNull();
            assertThat(cerrado).isEqualTo(new PeriodoInspeccion(inicio, inicio.plusHours(1)));
        }

        @Test
        @DisplayName("La fecha de fin no puede ser anterior a la de inicio")
        void finAnteriorAInicio() {
            assertThatThrownBy(() -> PeriodoInspeccion.iniciadoEn(inicio).finalizadoEn(inicio.minusMinutes(1)))
                    .isInstanceOf(DatoInvalidoException.class);
        }

        @Test
        @DisplayName("No puede existir fecha de fin sin fecha de inicio")
        void finSinInicio() {
            assertThatThrownBy(() -> new PeriodoInspeccion(null, inicio)).isInstanceOf(DatoInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("TipoInspeccion")
    class TipoInspeccionTest {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Un tipo ausente equivale a INSPECCION")
        void porDefecto(String valor) {
            assertThat(TipoInspeccion.desde(valor)).isEqualTo(TipoInspeccion.INSPECCION);
        }

        @Test
        @DisplayName("Interpreta el tipo sin distinguir mayúsculas")
        void interpreta() {
            assertThat(TipoInspeccion.desde(" reinspeccion ")).isEqualTo(TipoInspeccion.REINSPECCION);
        }

        @Test
        @DisplayName("Rechaza tipos desconocidos")
        void desconocido() {
            assertThatThrownBy(() -> TipoInspeccion.desde("MANTENIMIENTO"))
                    .isInstanceOf(DatoInvalidoException.class)
                    .hasMessageContaining("MANTENIMIENTO");
        }
    }

    @Nested
    @DisplayName("ResultadoInspeccion")
    class ResultadoInspeccionTest {

        @Test
        @DisplayName("Solo la condición APTO es apta")
        void esApto() {
            assertThat(ResultadoInspeccion.APTO.esApto()).isTrue();
            assertThat(ResultadoInspeccion.OBSERVADO.esApto()).isFalse();
        }

        @Test
        @DisplayName("La condición es obligatoria")
        void condicionObligatoria() {
            assertThatThrownBy(() -> new ResultadoInspeccion(null)).isInstanceOf(DatoInvalidoException.class);
        }
    }
}
