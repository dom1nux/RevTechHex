package org.parangaricutirimicuaro.revtech.adapter.in.web.identidad;

import jakarta.validation.Valid;
import org.parangaricutirimicuaro.revtech.adapter.in.web.identidad.dto.RegistroRolDto;
import org.parangaricutirimicuaro.revtech.adapter.in.web.identidad.dto.RolResponseDto;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarRolUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarRolUseCase.RegistrarRolCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada: expone los casos de uso de roles en {@code /api/roles}.
 */
@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RegistrarRolUseCase registrarRol;

    public RolController(RegistrarRolUseCase registrarRol) {
        this.registrarRol = registrarRol;
    }

    @PostMapping
    public ResponseEntity<RolResponseDto> registrar(@Valid @RequestBody RegistroRolDto dto) {
        var creado = registrarRol.registrar(new RegistrarRolCommand(dto.nombreRol(), dto.permisos()));
        return ResponseEntity.status(HttpStatus.CREATED).body(RolResponseDto.from(creado));
    }
}
