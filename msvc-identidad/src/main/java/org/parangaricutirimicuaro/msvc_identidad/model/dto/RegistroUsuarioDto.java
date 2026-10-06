package org.parangaricutirimicuaro.msvc_identidad.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistroUsuarioDto(
        @NotBlank(message = "username es obligatorio")
        @Size(min = 3, max = 50, message = "username 3-50 caracteres")
        String username,

        @NotBlank(message = "password es obligatorio")
        @Size(min = 8, max = 100, message = "password 8-100 caracteres")
        String password,

        @NotNull(message = "rolActivoId es obligatorio")
        Long rolActivoId
) {}
