package org.parangaricutirimicuaro.revtech.application.identidad.port.in;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;

import java.util.Optional;

public interface ConsultarUsuarioUseCase {

    Optional<UsuarioDetalle> buscarPorId(Long idUsuario);

    /**
     * Usuario junto con el nombre de su rol activo ({@code null} si no tiene rol o el rol ya no existe).
     */
    record UsuarioDetalle(Usuario usuario, NombreRol nombreRol) {}
}
