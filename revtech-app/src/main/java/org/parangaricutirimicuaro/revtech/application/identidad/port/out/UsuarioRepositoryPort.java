package org.parangaricutirimicuaro.revtech.application.identidad.port.out;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.UsuarioId;

import java.util.Optional;

/**
 * Puerto de salida: guarda y recupera usuarios desde el almacenamiento.
 */
public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(UsuarioId id);

    boolean existePorUsername(String username);
}
