package org.parangaricutirimicuaro.revtech.domain.identidad.model;

import org.parangaricutirimicuaro.revtech.domain.identidad.exception.DatoIdentidadInvalidoException;

/**
 * Cuenta de acceso de un miembro del personal; referencia su rol activo por identificador.
 */
public class Usuario {

    private final UsuarioId id;
    private final String username;
    private String passwordHash;
    private RolId rolActivo;
    private boolean estadoActivo;

    private Usuario(UsuarioId id, String username, String passwordHash, RolId rolActivo, boolean estadoActivo) {
        if (username == null || username.isBlank()) {
            throw new DatoIdentidadInvalidoException("username no puede ser nulo o vacío");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new DatoIdentidadInvalidoException("passwordHash no puede ser nulo o vacío");
        }
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rolActivo = rolActivo;
        this.estadoActivo = estadoActivo;
    }

    public static Usuario registrar(String username, String passwordHash, RolId rolActivo) {
        return new Usuario(null, username, passwordHash, rolActivo, true);
    }

    /**
     * Recrea un usuario ya persistido.
     */
    public static Usuario reconstituir(UsuarioId id, String username, String passwordHash, RolId rolActivo,
                                       boolean estadoActivo) {
        if (id == null) {
            throw new IllegalArgumentException("Solo se puede reconstituir un usuario ya persistido");
        }
        return new Usuario(id, username, passwordHash, rolActivo, estadoActivo);
    }

    public void cambiarContrasena(String nuevoHash) {
        if (nuevoHash == null || nuevoHash.isBlank()) {
            throw new DatoIdentidadInvalidoException("El nuevo hash no puede ser nulo o vacío");
        }
        this.passwordHash = nuevoHash;
    }

    public void cambiarEstadoActivo(boolean estado) {
        this.estadoActivo = estado;
    }

    public void asignarRol(RolId rol) {
        if (rol == null) {
            throw new DatoIdentidadInvalidoException("rolId no puede ser nulo");
        }
        this.rolActivo = rol;
    }

    public UsuarioId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public RolId getRolActivo() {
        return rolActivo;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }
}
