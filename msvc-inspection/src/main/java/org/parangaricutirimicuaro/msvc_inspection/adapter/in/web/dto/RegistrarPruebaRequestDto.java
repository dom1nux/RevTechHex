package org.parangaricutirimicuaro.msvc_inspection.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarPruebaRequestDto(
        @NotBlank(message = "El nombre de la prueba es obligatorio")
        String prueba,

        @NotBlank(message = "El resultado de la prueba es obligatorio (APROBADO o RECHAZADO)")
        String resultado
) {}
