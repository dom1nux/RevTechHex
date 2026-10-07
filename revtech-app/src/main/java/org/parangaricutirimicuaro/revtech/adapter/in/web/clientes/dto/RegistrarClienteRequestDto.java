package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarClienteRequestDto(
        @NotBlank(message = "El documento de identidad es obligatorio")
        String docIdent,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre
) {}
