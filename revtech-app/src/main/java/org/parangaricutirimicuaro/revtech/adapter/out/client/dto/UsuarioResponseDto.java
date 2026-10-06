package org.parangaricutirimicuaro.revtech.adapter.out.client.dto;

public record UsuarioResponseDto(
        Long idUsuario,
        String username,
        String nombreRol,
        Boolean estadoActivo
) {}
