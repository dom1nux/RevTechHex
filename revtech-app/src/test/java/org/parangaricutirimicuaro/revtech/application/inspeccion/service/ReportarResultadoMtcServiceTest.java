package org.parangaricutirimicuaro.revtech.application.inspeccion.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.MtcPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.VehiculoPort;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.VehiculoPort.VehiculoInfo;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.event.InspeccionFinalizada;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CondicionInspeccion;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionFixtures.*;

@ExtendWith(MockitoExtension.class)
class ReportarResultadoMtcServiceTest {

    @Mock
    private VehiculoPort vehiculoPort;
    @Mock
    private MtcPort mtcPort;

    private ReportarResultadoMtcService service;

    private final InspeccionFinalizada evento = new InspeccionFinalizada(ID, VEHICULO, CondicionInspeccion.OBSERVADO, FIN);

    @BeforeEach
    void setUp() {
        service = new ReportarResultadoMtcService(vehiculoPort, mtcPort);
    }

    @Test
    @DisplayName("Reporta al MTC la placa del vehículo y la condición obtenida")
    void reporta() {
        when(vehiculoPort.buscarPorId(VEHICULO)).thenReturn(Optional.of(new VehiculoInfo(VEHICULO, "ABC-123")));

        service.alFinalizarInspeccion(evento);

        verify(mtcPort).reportarResultado(ID, "ABC-123", CondicionInspeccion.OBSERVADO);
    }

    @Test
    @DisplayName("Si el vehículo ya no se encuentra, reporta con placa DESCONOCIDA")
    void placaDesconocida() {
        when(vehiculoPort.buscarPorId(VEHICULO)).thenReturn(Optional.empty());

        service.alFinalizarInspeccion(evento);

        verify(mtcPort).reportarResultado(ID, ReportarResultadoMtcService.PLACA_DESCONOCIDA, CondicionInspeccion.OBSERVADO);
    }

    @Test
    @DisplayName("Un fallo del MTC o de Clientes no se propaga: el reporte es de mejor esfuerzo")
    void mejorEsfuerzo() {
        when(vehiculoPort.buscarPorId(VEHICULO)).thenReturn(Optional.of(new VehiculoInfo(VEHICULO, "ABC-123")));
        doThrow(new RuntimeException("MTC fuera de línea")).when(mtcPort).reportarResultado(any(), any(), any());

        assertThatCode(() -> service.alFinalizarInspeccion(evento)).doesNotThrowAnyException();

        when(vehiculoPort.buscarPorId(VEHICULO)).thenThrow(new RuntimeException("Clientes caído"));
        assertThatCode(() -> service.alFinalizarInspeccion(evento)).doesNotThrowAnyException();
    }
}
