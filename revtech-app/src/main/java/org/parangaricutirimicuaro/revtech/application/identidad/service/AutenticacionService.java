package org.parangaricutirimicuaro.revtech.application.identidad.service;

import org.parangaricutirimicuaro.revtech.application.identidad.port.in.AutenticarUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.RolAccesoRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.UsuarioRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.CredencialesInvalidasException;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.RecursoIdentidadNoEncontradoException;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.ReglaIdentidadVioladaException;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolUsuario;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Base64;

/**
 * Autenticación con token simulado (Base64 de {@code username|rol|permisos|timestamp}).
 */
public class AutenticacionService implements AutenticarUseCase {

    private final UsuarioRepositoryPort usuarios;
    private final RolAccesoRepositoryPort roles;
    private final Clock clock;

    public AutenticacionService(UsuarioRepositoryPort usuarios, RolAccesoRepositoryPort roles, Clock clock) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.clock = clock;
    }

    @Override
    public String login(String username, String password) {
        Usuario usuario = usuarios.buscarPorUsername(username).orElseThrow(CredencialesInvalidasException::new);
        if (!usuario.isEstadoActivo()) {
            throw new ReglaIdentidadVioladaException("Usuario suspendido / inactivo");
        }
        if (!usuario.getPasswordHash().equals(password)) {
            throw new CredencialesInvalidasException();
        }
        if (usuario.getRolActivo() == null) {
            throw new ReglaIdentidadVioladaException("Usuario sin rol asignado");
        }
        RolAcceso rol = roles.buscarPorId(usuario.getRolActivo())
                .orElseThrow(() -> new RecursoIdentidadNoEncontradoException(
                        "Rol asignado no existe: " + usuario.getRolActivo().valor()));
        if (!rol.isEstadoActivo()) {
            throw new ReglaIdentidadVioladaException("Rol inactivo: " + rol.getNombreRol());
        }
        return generarTokenSimulado(usuario.getUsername(), rol.comoRolUsuario());
    }

    @Override
    public boolean validarToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            // Formato esperado: username|rol|perm1,perm2|timestamp
            String[] parts = decoded.split("\\|");
            if (parts.length < 3) return false;
            return usuarios.buscarPorUsername(parts[0])
                    .map(u -> u.isEstadoActivo() && u.getRolActivo() != null)
                    .orElse(false);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String generarTokenSimulado(String username, RolUsuario rolUsuario) {
        String payload = String.join("|",
                username,
                rolUsuario.rol(),
                String.join(",", rolUsuario.permisos()),
                String.valueOf(clock.millis()));
        return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }
}
