package org.parangaricutirimicuaro.msvc_identidad.service;

import org.parangaricutirimicuaro.msvc_identidad.model.dto.RegistroUsuarioDto;
import org.parangaricutirimicuaro.msvc_identidad.model.dto.UsuarioResponseDto;

public interface UsuarioService {

    UsuarioResponseDto registrar(RegistroUsuarioDto dto);

    UsuarioResponseDto suspenderUsuario(Long idUsuario);

    UsuarioResponseDto actualizarRol(Long idUsuario, Long nuevoRolId);

    UsuarioResponseDto buscarPorId(Long idUsuario);
}
