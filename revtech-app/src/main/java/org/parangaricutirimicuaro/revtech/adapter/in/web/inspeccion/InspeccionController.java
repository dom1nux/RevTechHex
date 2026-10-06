package org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion.dto.CrearInspeccionRequestDto;
import org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion.dto.FinalizarInspeccionRequestDto;
import org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion.dto.InspeccionResponseDto;
import org.parangaricutirimicuaro.revtech.adapter.in.web.inspeccion.dto.RegistrarPruebaRequestDto;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.ConsultarInspeccionUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.CrearInspeccionUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.CrearInspeccionUseCase.CrearInspeccionCommand;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.FinalizarInspeccionUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.IniciarInspeccionUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.RegistrarPruebaUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.in.RegistrarPruebaUseCase.RegistrarPruebaCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de entrada: traduce las peticiones HTTP de {@code /api/inspecciones} en llamadas a los casos de uso de Inspección.
 */
@RestController
@RequestMapping("/api/inspecciones")
@Tag(name = "Inspección Técnica Vehicular", description = "Endpoints para la gestión del flujo core de inspecciones técnicas vehiculares")
public class InspeccionController {

    private final CrearInspeccionUseCase crearInspeccion;
    private final IniciarInspeccionUseCase iniciarInspeccion;
    private final RegistrarPruebaUseCase registrarPrueba;
    private final FinalizarInspeccionUseCase finalizarInspeccion;
    private final ConsultarInspeccionUseCase consultarInspeccion;

    public InspeccionController(CrearInspeccionUseCase crearInspeccion,
                                IniciarInspeccionUseCase iniciarInspeccion,
                                RegistrarPruebaUseCase registrarPrueba,
                                FinalizarInspeccionUseCase finalizarInspeccion,
                                ConsultarInspeccionUseCase consultarInspeccion) {
        this.crearInspeccion = crearInspeccion;
        this.iniciarInspeccion = iniciarInspeccion;
        this.registrarPrueba = registrarPrueba;
        this.finalizarInspeccion = finalizarInspeccion;
        this.consultarInspeccion = consultarInspeccion;
    }

    @PostMapping
    @Operation(summary = "Crear nueva inspección", description = "Registra una nueva inspección técnica validando vehículo e inspector")
    public ResponseEntity<InspeccionResponseDto> crear(@Valid @RequestBody CrearInspeccionRequestDto request) {
        var command = new CrearInspeccionCommand(request.idVehiculo(), request.idInspector(),
                request.idSupervisor(), request.tipoInspeccion(), request.idInspeccionOrigen());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InspeccionWebMapper.toResponse(crearInspeccion.crear(command)));
    }

    @PutMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar proceso de inspección", description = "Cambia el estado a EN_PROCESO y marca la fecha/hora de inicio")
    public ResponseEntity<InspeccionResponseDto> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(InspeccionWebMapper.toResponse(iniciarInspeccion.iniciar(id)));
    }

    @PostMapping("/{id}/pruebas")
    @Operation(summary = "Registrar resultado de prueba", description = "Agrega una evaluación técnica (frenos, emisiones, dirección, etc.) a la inspección en curso")
    public ResponseEntity<InspeccionResponseDto> registrarPrueba(
            @PathVariable Long id,
            @Valid @RequestBody RegistrarPruebaRequestDto request) {
        var command = new RegistrarPruebaCommand(id, request.prueba(), request.resultado());
        return ResponseEntity.ok(InspeccionWebMapper.toResponse(registrarPrueba.registrar(command)));
    }

    @PutMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar inspección", description = "Evalúa todas las pruebas registradas y emite automáticamente Certificado (Apto) o Acta de Observaciones (Observado)")
    public ResponseEntity<InspeccionResponseDto> finalizar(
            @PathVariable Long id,
            @RequestBody(required = false) FinalizarInspeccionRequestDto request) {
        String observaciones = (request != null) ? request.observaciones() : null;
        return ResponseEntity.ok(InspeccionWebMapper.toResponse(finalizarInspeccion.finalizar(id, observaciones)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar inspección por ID", description = "Obtiene los datos completos de la inspección técnica, sus pruebas y documento emitido")
    public ResponseEntity<InspeccionResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(InspeccionWebMapper.toResponse(consultarInspeccion.obtenerPorId(id)));
    }
}
