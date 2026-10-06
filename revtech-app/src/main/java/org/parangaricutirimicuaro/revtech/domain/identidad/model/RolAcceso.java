package org.parangaricutirimicuaro.revtech.domain.identidad.model;

import org.parangaricutirimicuaro.revtech.domain.identidad.exception.DatoIdentidadInvalidoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rol de acceso con su conjunto de permisos.
 */
public class RolAcceso {

    private final RolId id;
    private final NombreRol nombreRol;
    private final List<String> permisos;
    private boolean estadoActivo;

    private RolAcceso(RolId id, NombreRol nombreRol, List<String> permisos, boolean estadoActivo) {
        if (nombreRol == null) {
            throw new DatoIdentidadInvalidoException("nombreRol no puede ser nulo");
        }
        this.id = id;
        this.nombreRol = nombreRol;
        this.permisos = permisos == null ? new ArrayList<>() : new ArrayList<>(permisos);
        this.estadoActivo = estadoActivo;
    }

    public static RolAcceso crear(NombreRol nombreRol, List<String> permisos) {
        return new RolAcceso(null, nombreRol, permisos, true);
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

    public void asignarPermisos(List<String> nuevosPermisos) {
        if (nuevosPermisos == null) {
            throw new DatoIdentidadInvalidoException("Los permisos no pueden ser nulos (use lista vacía para limpiar)");
        }
        for (String p : nuevosPermisos) {
            if (p == null || p.isBlank()) {
                throw new DatoIdentidadInvalidoException("Un permiso no puede ser nulo o vacío");
            }
        }
        this.permisos.clear();
        this.permisos.addAll(nuevosPermisos);
    }

    public void desactivarRol() {
        this.estadoActivo = false;
    }

    public void activarRol() {
        this.estadoActivo = true;
    }

    public RolUsuario comoRolUsuario() {
        return new RolUsuario(nombreRol.name(), permisos);
    }

    public RolId getId() {
        return id;
    }

    public NombreRol getNombreRol() {
        return nombreRol;
    }

    public List<String> getPermisos() {
        return Collections.unmodifiableList(permisos);
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }
}
