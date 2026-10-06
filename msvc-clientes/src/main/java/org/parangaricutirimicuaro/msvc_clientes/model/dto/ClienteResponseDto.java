package org.parangaricutirimicuaro.msvc_clientes.model.dto;

import org.parangaricutirimicuaro.msvc_clientes.model.entity.Cliente;

import java.time.LocalDateTime;

public record ClienteResponseDto(
        Long idCliente,
        String docIdent,
        String nombre,
        LocalDateTime fechaRegistro
) {
    public static ClienteResponseDto fromEntity(Cliente c) {
        return new ClienteResponseDto(
                c.getIdCliente(),
                c.getDocIdent(),
                c.getNombre(),
                c.getFechaRegistro()
        );
    }
}
