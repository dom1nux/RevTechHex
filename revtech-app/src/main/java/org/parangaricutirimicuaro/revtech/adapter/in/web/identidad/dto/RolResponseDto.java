package org.parangaricutirimicuaro.revtech.adapter.in.web.identidad.dto;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;

import java.util.List;

public record RolResponseDto(
        Long idRol,
        String nombreRol,
        List<String> permisos,
        boolean estadoActivo
) {
    public static RolResponseDto from(RolAcceso r) {
        return new RolResponseDto(r.getId().valor(), r.getNombreRol().name(), r.getPermisos(), r.isEstadoActivo());
    }
}
