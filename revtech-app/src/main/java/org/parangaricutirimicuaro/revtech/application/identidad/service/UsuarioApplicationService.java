package org.parangaricutirimicuaro.revtech.application.identidad.service;

import org.parangaricutirimicuaro.revtech.application.identidad.port.in.ConsultarUsuarioUseCase;
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

/**
 * Servicio de aplicación de usuarios: implementa los casos de uso usando solo puertos. Se registra como bean en {@code IdentidadConfig}.
 */
public class UsuarioApplicationService implements RegistrarUsuarioUseCase, ConsultarUsuarioUseCase {

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
        RolAcceso rol = roles.buscarPorId(RolId.of(command.rolActivoId()))
                .orElseThrow(() -> new RecursoIdentidadNoEncontradoException("Rol con id " + command.rolActivoId() + " no existe"));
        if (!rol.isEstadoActivo()) {
            throw new ReglaIdentidadVioladaException("No se puede asignar un rol inactivo: " + rol.getNombreRol());
        }
        // TODO: reemplazar por passwordEncoder.encode(command.password()); por ahora se guarda directo
        String hashSimulado = command.password();
        return usuarios.guardar(Usuario.registrar(command.username(), hashSimulado, rol.getId()));
    }

    @Override
    public Optional<UsuarioDetalle> buscarPorId(Long idUsuario) {
        return usuarios.buscarPorId(UsuarioId.of(idUsuario)).map(usuario -> new UsuarioDetalle(usuario,
                Optional.ofNullable(usuario.getRolActivo())
                        .flatMap(roles::buscarPorId)
                        .map(RolAcceso::getNombreRol)
                        .orElse(null)));
    }
}
