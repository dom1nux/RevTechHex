package org.parangaricutirimicuaro.msvc_clientes.controller;

import jakarta.validation.Valid;
import org.parangaricutirimicuaro.msvc_clientes.model.dto.CrearVehiculoRequestDto;
import org.parangaricutirimicuaro.msvc_clientes.model.dto.VehiculoResponseDto;
import org.parangaricutirimicuaro.msvc_clientes.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping
    public ResponseEntity<VehiculoResponseDto> crear(@Valid @RequestBody CrearVehiculoRequestDto request) {
        VehiculoResponseDto creado = vehiculoService.crearVehiculo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{idVehiculo}")
    public ResponseEntity<VehiculoResponseDto> obtenerPorId(@PathVariable("idVehiculo") Long idVehiculo) {
        VehiculoResponseDto dto = vehiculoService.buscarPorId(idVehiculo);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<VehiculoResponseDto> obtenerPorPlaca(@PathVariable("placa") String placa) {
        VehiculoResponseDto dto = vehiculoService.buscarPorPlaca(placa);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
