package org.parangaricutirimicuaro.msvc_clientes.service.impl;

import org.parangaricutirimicuaro.msvc_clientes.model.dto.CrearVehiculoRequestDto;
import org.parangaricutirimicuaro.msvc_clientes.model.dto.VehiculoResponseDto;
import org.parangaricutirimicuaro.msvc_clientes.model.entity.Vehiculo;
import org.parangaricutirimicuaro.msvc_clientes.repository.VehiculoRepository;
import org.parangaricutirimicuaro.msvc_clientes.service.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    @Transactional
    public VehiculoResponseDto crearVehiculo(CrearVehiculoRequestDto request) {
        Vehiculo vehiculo = new Vehiculo(
                request.clienteId(),
                request.placa(),
                request.categoria(),
                request.marca(),
                request.modelo(),
                request.anioFabricacion()
        );
        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return VehiculoResponseDto.fromEntity(guardado);
    }

    @Override
    public VehiculoResponseDto buscarPorId(Long idVehiculo) {
        return vehiculoRepository.findById(idVehiculo)
                .map(VehiculoResponseDto::fromEntity)
                .orElse(null);
    }

    @Override
    public VehiculoResponseDto buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
                .map(VehiculoResponseDto::fromEntity)
                .orElse(null);
    }

    @Override
    public List<VehiculoResponseDto> listarPorCliente(Long clienteId) {
        return vehiculoRepository.findByClienteId(clienteId)
                .stream()
                .map(VehiculoResponseDto::fromEntity)
                .toList();
    }
}
