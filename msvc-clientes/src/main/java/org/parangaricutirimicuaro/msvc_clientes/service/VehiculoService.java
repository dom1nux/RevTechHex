package org.parangaricutirimicuaro.msvc_clientes.service;

import org.parangaricutirimicuaro.msvc_clientes.model.dto.CrearVehiculoRequestDto;
import org.parangaricutirimicuaro.msvc_clientes.model.dto.VehiculoResponseDto;

import java.util.List;

public interface VehiculoService {
    VehiculoResponseDto crearVehiculo(CrearVehiculoRequestDto request);
    VehiculoResponseDto buscarPorId(Long idVehiculo);
    VehiculoResponseDto buscarPorPlaca(String placa);
    List<VehiculoResponseDto> listarPorCliente(Long clienteId);
}
