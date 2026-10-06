package org.parangaricutirimicuaro.revtech.application.identidad.service;

import org.parangaricutirimicuaro.revtech.application.identidad.port.in.ConsultarUsuarioUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.GestionarUsuarioUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarUsuarioUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.RolAccesoRepositoryPort;
import org.parangaricutirimicuaro.revtech.application.identidad.port.out.UsuarioRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.RecursoIdentidadNoEncontradoException;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.ReglaIdentidadVioladaException;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.UsuarioId;

import java.util.Optional;

public class UsuarioApplicationService implements RegistrarUsuarioUseCase, GestionarUsuarioUseCase, ConsultarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarios;
    private final RolAccesoRepositoryPort roles;

    public UsuarioApplicationService(UsuarioRepositoryPort usuarios, RolAccesoRepositoryPort roles) {
        this.usuarios = usuarios;
        this.roles = roles;
    }

    @Override
    public Usuario registrar(RegistrarUsuarioCommand command) {
        if (usuarios.existePorUsername(command.username())) {
            throw new ReglaIdentidadVioladaException("El username '" + command.username() + "' ya existe");
        }
        RolAcceso rol = rolActivo(command.rolActivoId());
        // TODO: reemplazar por passwordEncoder.encode(command.password()); por ahora se guarda directo
        String hashSimulado = command.password();
        return usuarios.guardar(Usuario.registrar(command.username(), hashSimulado, rol.getId()));
    }

    @Override
    public Usuario suspender(Long idUsuario) {
        Usuario usuario = obtener(idUsuario);
        usuario.cambiarEstadoActivo(false);
        return usuarios.guardar(usuario);
    }

    @Override
    public Usuario actualizarRol(Long idUsuario, Long nuevoRolId) {
        Usuario usuario = obtener(idUsuario);
        usuario.asignarRol(rolActivo(nuevoRolId).getId());
        return usuarios.guardar(usuario);
    }

    @Override
    public Optional<UsuarioDetalle> buscarPorId(Long idUsuario) {
        return usuarios.buscarPorId(UsuarioId.of(idUsuario)).map(usuario -> new UsuarioDetalle(usuario,
                Optional.ofNullable(usuario.getRolActivo())
                        .flatMap(roles::buscarPorId)
                        .map(RolAcceso::getNombreRol)
                        .orElse(null)));
    }

    private Usuario obtener(Long idUsuario) {
        return usuarios.buscarPorId(UsuarioId.of(idUsuario))
                .orElseThrow(() -> new RecursoIdentidadNoEncontradoException("Usuario con id " + idUsuario + " no encontrado"));
    }

    private RolAcceso rolActivo(Long idRol) {
        RolAcceso rol = roles.buscarPorId(RolId.of(idRol))
                .orElseThrow(() -> new RecursoIdentidadNoEncontradoException("Rol con id " + idRol + " no existe"));
        if (!rol.isEstadoActivo()) {
            throw new ReglaIdentidadVioladaException("No se puede asignar un rol inactivo: " + rol.getNombreRol());
        }
        return rol;
    }
}
