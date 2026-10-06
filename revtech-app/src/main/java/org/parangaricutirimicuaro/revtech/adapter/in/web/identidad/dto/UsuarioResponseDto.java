package org.parangaricutirimicuaro.revtech.adapter.in.web.identidad.dto;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;

public record UsuarioResponseDto(
        Long idUsuario,
        String username,
        Long rolActivoId,
        String nombreRol,
        boolean estadoActivo
) {
    public static UsuarioResponseDto from(Usuario u, NombreRol nombreRol) {
        return new UsuarioResponseDto(
                u.getId().valor(),
                u.getUsername(),
                u.getRolActivo() != null ? u.getRolActivo().valor() : null,
                nombreRol != null ? nombreRol.name() : null,
                u.isEstadoActivo()
        );
    }
}
