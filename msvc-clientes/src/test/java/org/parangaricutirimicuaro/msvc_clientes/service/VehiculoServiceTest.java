package org.parangaricutirimicuaro.msvc_clientes.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.msvc_clientes.model.dto.VehiculoResponseDto;
import org.parangaricutirimicuaro.msvc_clientes.model.entity.Vehiculo;
import org.parangaricutirimicuaro.msvc_clientes.model.type.CategoriaVehiculo;
import org.parangaricutirimicuaro.msvc_clientes.repository.VehiculoRepository;
import org.parangaricutirimicuaro.msvc_clientes.service.impl.VehiculoServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private VehiculoServiceImpl vehiculoService;

    @Test
    @DisplayName("Debe retornar DTO cuando el vehículo existe por ID")
    void buscarPorId_existente_retornaDto() {
        Vehiculo vehiculo = new Vehiculo(10L, "ABC-123", CategoriaVehiculo.M1, "Toyota", "Yaris", 2020);
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        VehiculoResponseDto resultado = vehiculoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("ABC-123", resultado.placa());
        assertEquals("M1", resultado.categoria());
        assertEquals("Toyota", resultado.marca());
        assertEquals(10L, resultado.clienteId());
    }

    @Test
    @DisplayName("Debe retornar null cuando el vehículo no existe por ID")
    void buscarPorId_inexistente_retornaNull() {
        when(vehiculoRepository.findById(99L)).thenReturn(Optional.empty());

        VehiculoResponseDto resultado = vehiculoService.buscarPorId(99L);

        assertNull(resultado);
    }

    @Test
    @DisplayName("Debe buscar vehículo correctamente por placa")
    void buscarPorPlaca_existente_retornaDto() {
        Vehiculo vehiculo = new Vehiculo(15L, "XYZ-789", CategoriaVehiculo.N1, "Nissan", "Navara", 2022);
        when(vehiculoRepository.findByPlaca("XYZ-789")).thenReturn(Optional.of(vehiculo));

        VehiculoResponseDto resultado = vehiculoService.buscarPorPlaca("XYZ-789");

        assertNotNull(resultado);
        assertEquals("XYZ-789", resultado.placa());
        assertEquals("N1", resultado.categoria());
    }

    @Test
    @DisplayName("Debe listar vehículos por cliente")
    void listarPorCliente_retornaLista() {
        Vehiculo v1 = new Vehiculo(5L, "P1A-100", CategoriaVehiculo.M1, "Kia", "Rio", 2019);
        when(vehiculoRepository.findByClienteId(5L)).thenReturn(List.of(v1));

        List<VehiculoResponseDto> resultado = vehiculoService.listarPorCliente(5L);

        assertEquals(1, resultado.size());
        assertEquals("P1A-100", resultado.get(0).placa());
    }

    @Test
    @DisplayName("Debe crear un vehículo correctamente")
    void crearVehiculo_valido_guardaYRetornaDto() {
        org.parangaricutirimicuaro.msvc_clientes.model.dto.CrearVehiculoRequestDto req =
                new org.parangaricutirimicuaro.msvc_clientes.model.dto.CrearVehiculoRequestDto(
                        20L, "ABC-999", CategoriaVehiculo.M1, "Toyota", "Corolla", 2021
                );
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehiculoResponseDto resultado = vehiculoService.crearVehiculo(req);

        assertNotNull(resultado);
        assertEquals("ABC-999", resultado.placa());
        assertEquals(20L, resultado.clienteId());
    }
}
