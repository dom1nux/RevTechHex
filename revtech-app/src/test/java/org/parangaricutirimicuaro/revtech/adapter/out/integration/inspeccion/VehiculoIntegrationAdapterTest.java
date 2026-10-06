package org.parangaricutirimicuaro.revtech.adapter.out.integration.inspeccion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarVehiculoUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.VehiculoPort.VehiculoInfo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.CategoriaVehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.VehiculoId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculoIntegrationAdapterTest {

    @Mock
    private ConsultarVehiculoUseCase consultarVehiculo;

    @Test
    @DisplayName("Traduce el vehículo de Atención a Clientes a la vista mínima que necesita Inspección")
    void vehiculoEncontrado() {
        when(consultarVehiculo.buscarPorId(1L)).thenReturn(Optional.of(Vehiculo.reconstituir(
                org.parangaricutirimicuaro.revtech.domain.clientes.model.VehiculoId.of(1L), ClienteId.of(10L),
                "ABC-123", CategoriaVehiculo.M1, "Toyota", "Corolla", 2020)));

        assertThat(new VehiculoIntegrationAdapter(consultarVehiculo).buscarPorId(VehiculoId.of(1L)))
                .contains(new VehiculoInfo(VehiculoId.of(1L), "ABC-123"));
    }

    @Test
    @DisplayName("Un vehículo inexistente devuelve vacío")
    void vehiculoInexistente() {
        when(consultarVehiculo.buscarPorId(2L)).thenReturn(Optional.empty());

        assertThat(new VehiculoIntegrationAdapter(consultarVehiculo).buscarPorId(VehiculoId.of(2L))).isEmpty();
    }
}
