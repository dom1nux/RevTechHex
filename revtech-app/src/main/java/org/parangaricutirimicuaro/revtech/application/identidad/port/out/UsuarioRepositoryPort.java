package org.parangaricutirimicuaro.revtech.application.identidad.port.out;

import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.UsuarioId;

import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(UsuarioId id);

    Optional<Usuario> buscarPorUsername(String username);

    boolean existePorUsername(String username);
}
