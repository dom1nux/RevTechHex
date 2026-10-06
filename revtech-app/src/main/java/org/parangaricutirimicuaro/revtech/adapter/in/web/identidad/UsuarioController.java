package org.parangaricutirimicuaro.revtech.adapter.in.web.identidad;

import jakarta.validation.Valid;
import org.parangaricutirimicuaro.revtech.adapter.in.web.identidad.dto.RegistroUsuarioDto;
import org.parangaricutirimicuaro.revtech.adapter.in.web.identidad.dto.UsuarioResponseDto;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.ConsultarUsuarioUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarUsuarioUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarUsuarioUseCase.RegistrarUsuarioCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de entrada: expone los casos de uso de usuarios en {@code /api/usuarios}.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuario;
    private final ConsultarUsuarioUseCase consultarUsuario;

    public UsuarioController(RegistrarUsuarioUseCase registrarUsuario, ConsultarUsuarioUseCase consultarUsuario) {
        this.registrarUsuario = registrarUsuario;
        this.consultarUsuario = consultarUsuario;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> registrar(@Valid @RequestBody RegistroUsuarioDto dto) {
        var creado = registrarUsuario.registrar(
                new RegistrarUsuarioCommand(dto.username(), dto.password(), dto.rolActivoId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDto.from(creado, null));
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponseDto> obtenerPorId(@PathVariable("idUsuario") Long idUsuario) {
        return ResponseEntity.of(consultarUsuario.buscarPorId(idUsuario)
                .map(detalle -> UsuarioResponseDto.from(detalle.usuario(), detalle.nombreRol())));
    }
}
