package org.parangaricutirimicuaro.msvc_identidad.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.parangaricutirimicuaro.msvc_identidad.model.dto.RegistroUsuarioDto;
import org.parangaricutirimicuaro.msvc_identidad.model.dto.UsuarioResponseDto;
import org.parangaricutirimicuaro.msvc_identidad.model.entity.RolAcceso;
import org.parangaricutirimicuaro.msvc_identidad.model.entity.Usuario;
import org.parangaricutirimicuaro.msvc_identidad.repository.RolAccesoRepository;
import org.parangaricutirimicuaro.msvc_identidad.repository.UsuarioRepository;
import org.parangaricutirimicuaro.msvc_identidad.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación con @Transactional.
 * Respeta regla estricta: usa métodos ricos de la entidad, nunca setters.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolAccesoRepository rolAccesoRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolAccesoRepository rolAccesoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolAccesoRepository = rolAccesoRepository;
    }

    @Override
    public UsuarioResponseDto registrar(RegistroUsuarioDto dto) {
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("El username '" + dto.username() + "' ya existe");
        }

        // Verificar que el rol exista (comunicación entre agregados por ID)
        RolAcceso rol = rolAccesoRepository.findById(dto.rolActivoId())
                .orElseThrow(() -> new EntityNotFoundException("Rol con id " + dto.rolActivoId() + " no existe"));

        if (!rol.isEstadoActivo()) {
            throw new IllegalStateException("No se puede asignar un rol inactivo: " + rol.getNombreRol());
        }

        // En producción aquí se hashearía con PasswordEncoder; Fase 2 lo simula guardando directo
        // Se invoca constructor de dominio, no setters
        String hashSimulado = dto.password(); // TODO: reemplazar por passwordEncoder.encode(dto.password())

        Usuario usuario = new Usuario(dto.username(), hashSimulado, rol.getIdRol());
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioResponseDto.fromEntity(guardado);
    }

    @Override
    public UsuarioResponseDto suspenderUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + idUsuario + " no encontrado"));

        // Regla estricta: usar método rico, no setter
        usuario.cambiarEstadoActivo(false);

        Usuario actualizado = usuarioRepository.save(usuario);
        return UsuarioResponseDto.fromEntity(actualizado);
    }

    @Override
    public UsuarioResponseDto actualizarRol(Long idUsuario, Long nuevoRolId) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + idUsuario + " no encontrado"));

        RolAcceso nuevoRol = rolAccesoRepository.findById(nuevoRolId)
                .orElseThrow(() -> new EntityNotFoundException("Rol con id " + nuevoRolId + " no existe"));

        if (!nuevoRol.isEstadoActivo()) {
            throw new IllegalStateException("No se puede asignar un rol inactivo: " + nuevoRol.getNombreRol());
        }

        // Método rico de dominio
        usuario.asignarRol(nuevoRol.getIdRol());

        Usuario actualizado = usuarioRepository.save(usuario);
        return UsuarioResponseDto.fromEntity(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + idUsuario + " no encontrado"));
        String nombreRol = null;
        if (usuario.getRolActivoId() != null) {
            nombreRol = rolAccesoRepository.findById(usuario.getRolActivoId())
                    .map(r -> r.getNombreRol().name())
                    .orElse(null);
        }
        return UsuarioResponseDto.fromEntity(usuario, nombreRol);
    }
}
