package org.parangaricutirimicuaro.revtech.adapter.in.web.identidad.dto;

import jakarta.validation.constraints.NotNull;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;

import java.util.List;

public record RegistroRolDto(
        @NotNull(message = "nombreRol es obligatorio")
        NombreRol nombreRol,

        List<String> permisos
) {}
