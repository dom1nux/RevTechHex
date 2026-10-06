package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto;

import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;

import java.time.LocalDateTime;

public record ClienteResponseDto(
        Long idCliente,
        String docIdent,
        String nombre,
        LocalDateTime fechaRegistro
) {
    public static ClienteResponseDto from(Cliente c) {
        return new ClienteResponseDto(
                c.getId().valor(),
                c.getDocIdent(),
                c.getNombre(),
                c.getFechaRegistro()
        );
    }
}
