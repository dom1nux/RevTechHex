package org.parangaricutirimicuaro.revtech.application.identidad.port.in;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;

public interface GestionarUsuarioUseCase {

    Usuario suspender(Long idUsuario);

    Usuario actualizarRol(Long idUsuario, Long nuevoRolId);
}
