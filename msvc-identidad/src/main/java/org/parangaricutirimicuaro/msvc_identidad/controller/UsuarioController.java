package org.parangaricutirimicuaro.msvc_identidad.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.parangaricutirimicuaro.msvc_identidad.model.dto.RegistroUsuarioDto;
import org.parangaricutirimicuaro.msvc_identidad.model.dto.UsuarioResponseDto;
import org.parangaricutirimicuaro.msvc_identidad.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> registrar(@Valid @RequestBody RegistroUsuarioDto dto) {
        UsuarioResponseDto creado = usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponseDto> obtenerPorId(@PathVariable("idUsuario") Long idUsuario) {
        try {
            UsuarioResponseDto dto = usuarioService.buscarPorId(idUsuario);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
