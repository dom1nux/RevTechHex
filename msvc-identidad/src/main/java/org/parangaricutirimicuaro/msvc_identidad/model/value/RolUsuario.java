package org.parangaricutirimicuaro.msvc_identidad.model.value;

import java.util.List;
public record RolUsuario(String rol, List<String> permisos) {
    public RolUsuario {
        if (rol == null || rol.isBlank()) {
            throw new IllegalArgumentException("El rol no puede ser nulo o vacío");
        }

        permisos = permisos == null ? List.of() : List.copyOf(permisos);
    }
}
