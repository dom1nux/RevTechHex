package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.dto;

public record UsuarioResponseDto(
        Long idUsuario,
        String username,
        String nombreRol,
        Boolean estadoActivo
) {}
