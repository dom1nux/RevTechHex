package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad.entity.RolAccesoJpaEntity;
import org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad.entity.UsuarioJpaEntity;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.UsuarioId;

import java.util.ArrayList;

/**
 * Traduce usuarios y roles de dominio a su representación JPA y viceversa.
 */
final class IdentidadPersistenceMapper {

    private IdentidadPersistenceMapper() {
    }

    static UsuarioJpaEntity toEntity(Usuario usuario) {
        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setIdUsuario(usuario.getId() != null ? usuario.getId().valor() : null);
        entity.setUsername(usuario.getUsername());
        entity.setPasswordHash(usuario.getPasswordHash());
        entity.setRolActivoId(usuario.getRolActivo() != null ? usuario.getRolActivo().valor() : null);
        entity.setEstadoActivo(usuario.isEstadoActivo());
        return entity;
    }

    static Usuario toDomain(UsuarioJpaEntity entity) {
        return Usuario.reconstituir(UsuarioId.of(entity.getIdUsuario()), entity.getUsername(),
                entity.getPasswordHash(), RolId.ofNullable(entity.getRolActivoId()), entity.isEstadoActivo());
    }

    static RolAccesoJpaEntity toEntity(RolAcceso rol) {
        RolAccesoJpaEntity entity = new RolAccesoJpaEntity();
        entity.setIdRol(rol.getId() != null ? rol.getId().valor() : null);
        entity.setNombreRol(rol.getNombreRol());
        entity.setPermisos(new ArrayList<>(rol.getPermisos()));
        entity.setEstadoActivo(rol.isEstadoActivo());
        return entity;
    }

    static RolAcceso toDomain(RolAccesoJpaEntity entity) {
        return RolAcceso.reconstituir(RolId.of(entity.getIdRol()), entity.getNombreRol(), entity.getPermisos(),
                entity.isEstadoActivo());
    }
}
