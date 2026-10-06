package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client;

import org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.dto.UsuarioResponseDto;

public interface UsuarioClient {
    UsuarioResponseDto obtenerUsuarioPorId(Long idUsuario);
}
