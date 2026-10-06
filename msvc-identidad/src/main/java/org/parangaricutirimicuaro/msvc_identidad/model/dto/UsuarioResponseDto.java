package org.parangaricutirimicuaro.msvc_identidad.model.dto;

import org.parangaricutirimicuaro.msvc_identidad.model.entity.Usuario;

public record UsuarioResponseDto(
        Long idUsuario,
        String username,
        Long rolActivoId,
        String nombreRol,
        boolean estadoActivo
) {
    public static UsuarioResponseDto fromEntity(Usuario u) {
        return new UsuarioResponseDto(
                u.getIdUsuario(),
                u.getUsername(),
                u.getRolActivoId(),
                null,
                u.isEstadoActivo()
        );
    }

    public static UsuarioResponseDto fromEntity(Usuario u, String nombreRol) {
        return new UsuarioResponseDto(
                u.getIdUsuario(),
                u.getUsername(),
                u.getRolActivoId(),
                nombreRol,
                u.isEstadoActivo()
        );
    }
}
