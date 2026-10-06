package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes;

import jakarta.validation.Valid;
import org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto.CrearVehiculoRequestDto;
import org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto.VehiculoResponseDto;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarVehiculoUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.CrearVehiculoUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.CrearVehiculoUseCase.CrearVehiculoCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de entrada: expone los casos de uso de vehículos en {@code /api/vehiculos}.
 */
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final CrearVehiculoUseCase crearVehiculo;
    private final ConsultarVehiculoUseCase consultarVehiculo;

    public VehiculoController(CrearVehiculoUseCase crearVehiculo, ConsultarVehiculoUseCase consultarVehiculo) {
        this.crearVehiculo = crearVehiculo;
        this.consultarVehiculo = consultarVehiculo;
    }

    @PostMapping
    public ResponseEntity<VehiculoResponseDto> crear(@Valid @RequestBody CrearVehiculoRequestDto request) {
        var command = new CrearVehiculoCommand(request.clienteId(), request.placa(), request.categoria(),
                request.marca(), request.modelo(), request.anioFabricacion());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(VehiculoResponseDto.from(crearVehiculo.crear(command)));
    }

    @GetMapping("/{idVehiculo}")
    public ResponseEntity<VehiculoResponseDto> obtenerPorId(@PathVariable("idVehiculo") Long idVehiculo) {
        return ResponseEntity.of(consultarVehiculo.buscarPorId(idVehiculo).map(VehiculoResponseDto::from));
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<VehiculoResponseDto> obtenerPorPlaca(@PathVariable("placa") String placa) {
        return ResponseEntity.of(consultarVehiculo.buscarPorPlaca(placa).map(VehiculoResponseDto::from));
    }
}
