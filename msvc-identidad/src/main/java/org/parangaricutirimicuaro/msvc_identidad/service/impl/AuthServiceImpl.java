package org.parangaricutirimicuaro.msvc_identidad.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.parangaricutirimicuaro.msvc_identidad.model.dto.LoginRequestDto;
import org.parangaricutirimicuaro.msvc_identidad.model.entity.RolAcceso;
import org.parangaricutirimicuaro.msvc_identidad.model.entity.Usuario;
import org.parangaricutirimicuaro.msvc_identidad.model.value.RolUsuario;
import org.parangaricutirimicuaro.msvc_identidad.repository.RolAccesoRepository;
import org.parangaricutirimicuaro.msvc_identidad.repository.UsuarioRepository;
import org.parangaricutirimicuaro.msvc_identidad.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolAccesoRepository rolAccesoRepository;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           RolAccesoRepository rolAccesoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolAccesoRepository = rolAccesoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public String login(LoginRequestDto dto) {
        Usuario usuario = usuarioRepository.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException("Credenciales inválidas"));

        if (!usuario.isEstadoActivo()) {
            throw new IllegalStateException("Usuario suspendido / inactivo");
        }

        if (!usuario.getPasswordHash().equals(dto.password())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        if (usuario.getRolActivoId() == null) {
            throw new IllegalStateException("Usuario sin rol asignado");
        }

        RolAcceso rol = rolAccesoRepository.findById(usuario.getRolActivoId())
                .orElseThrow(() -> new EntityNotFoundException("Rol asignado no existe: " + usuario.getRolActivoId()));

        if (!rol.isEstadoActivo()) {
            throw new IllegalStateException("Rol inactivo: " + rol.getNombreRol());
        }

        RolUsuario rolUsuario = new RolUsuario(
                rol.getNombreRol().name(),
                rol.getPermisos()
        );

        return generarTokenSimulado(usuario.getUsername(), rolUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            // Formato esperado: username|rol|perm1,perm2|timestamp
            String[] parts = decoded.split("\\|");
            if (parts.length < 3) return false;
            String username = parts[0];

            return usuarioRepository.findByUsername(username)
                    .map(u -> u.isEstadoActivo() && u.getRolActivoId() != null)
                    .orElse(false);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String generarTokenSimulado(String username, RolUsuario rolUsuario) {
        String permisosCsv = String.join(",", rolUsuario.permisos());
        String payload = String.join("|",
                username,
                rolUsuario.rol(),
                permisosCsv,
                String.valueOf(System.currentTimeMillis())
        );
        return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }
}
