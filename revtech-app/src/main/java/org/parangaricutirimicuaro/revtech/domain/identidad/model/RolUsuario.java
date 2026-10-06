package org.parangaricutirimicuaro.revtech.domain.identidad.model;

import org.parangaricutirimicuaro.revtech.domain.identidad.exception.DatoIdentidadInvalidoException;

import java.util.List;

/**
 * Rol efectivo de un usuario junto con sus permisos.
 */
public record RolUsuario(String rol, List<String> permisos) {

    public RolUsuario {
        if (rol == null || rol.isBlank()) {
            throw new DatoIdentidadInvalidoException("El rol no puede ser nulo o vacío");
        }
        permisos = permisos == null ? List.of() : List.copyOf(permisos);
    }
}
