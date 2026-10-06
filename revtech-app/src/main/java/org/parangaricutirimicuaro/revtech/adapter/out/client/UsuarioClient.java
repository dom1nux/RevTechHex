package org.parangaricutirimicuaro.revtech.adapter.out.client;

import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.UsuarioResponseDto;

public interface UsuarioClient {
    UsuarioResponseDto obtenerUsuarioPorId(Long idUsuario);
}
