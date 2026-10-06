package org.parangaricutirimicuaro.revtech.application.inspeccion.port.out;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PersonalId;

import java.util.Optional;

/**
 * Consulta de personal técnico en el contexto Identidad y Acceso.
 */
public interface UsuarioPort {

    Optional<UsuarioInfo> buscarPorId(PersonalId id);

    record UsuarioInfo(PersonalId id, String rol, boolean activo) {}
}
