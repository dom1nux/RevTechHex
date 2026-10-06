package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarClienteRequestDto(
        @NotBlank(message = "docIdent es obligatorio")
        String docIdent,

        @NotBlank(message = "nombre es obligatorio")
        String nombre
) {}
