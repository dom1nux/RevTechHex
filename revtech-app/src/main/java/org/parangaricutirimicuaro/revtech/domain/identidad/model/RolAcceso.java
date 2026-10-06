package org.parangaricutirimicuaro.revtech.domain.identidad.model;

import org.parangaricutirimicuaro.revtech.domain.identidad.exception.DatoIdentidadInvalidoException;

import java.util.List;

/**
 * Rol de acceso con su conjunto de permisos. Los roles se administran directamente en la base de datos.
 */
public class RolAcceso {

    private final RolId id;
    private final NombreRol nombreRol;
    private final List<String> permisos;
    private final boolean estadoActivo;

    private RolAcceso(RolId id, NombreRol nombreRol, List<String> permisos, boolean estadoActivo) {
        if (nombreRol == null) {
            throw new DatoIdentidadInvalidoException("nombreRol no puede ser nulo");
        }
        this.id = id;
        this.nombreRol = nombreRol;
        this.permisos = permisos == null ? List.of() : List.copyOf(permisos);
        this.estadoActivo = estadoActivo;
    }

    /**
     * Recrea un rol ya persistido.
     */
    public static RolAcceso reconstituir(RolId id, NombreRol nombreRol, List<String> permisos, boolean estadoActivo) {
        if (id == null) {
            throw new IllegalArgumentException("Solo se puede reconstituir un rol ya persistido");
        }
        return new RolAcceso(id, nombreRol, permisos, estadoActivo);
    }

    public RolId getId() {
        return id;
    }

    public NombreRol getNombreRol() {
        return nombreRol;
    }

    public List<String> getPermisos() {
        return permisos;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }
}
