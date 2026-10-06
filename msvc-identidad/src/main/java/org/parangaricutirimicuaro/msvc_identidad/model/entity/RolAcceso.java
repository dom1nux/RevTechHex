package org.parangaricutirimicuaro.msvc_identidad.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.parangaricutirimicuaro.msvc_identidad.model.type.NombreRol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "roles_acceso", uniqueConstraints = {
        @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombre_rol")
})
public class RolAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, name = "nombre_rol", length = 50)
    private NombreRol nombreRol;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rol_permisos", joinColumns = @JoinColumn(name = "rol_id"))
    @Column(name = "permiso", nullable = false, length = 100)
    private List<String> permisos = new ArrayList<>();

    @Column(nullable = false, name = "estado_activo")
    private boolean estadoActivo = true;

    public RolAcceso(NombreRol nombreRol, List<String> permisos) {
        if (nombreRol == null) {
            throw new IllegalArgumentException("nombreRol no puede ser nulo");
        }
        this.nombreRol = nombreRol;
        this.permisos = permisos == null ? new ArrayList<>() : new ArrayList<>(permisos);
        this.estadoActivo = true;
    }

    public void asignarPermisos(List<String> nuevosPermisos) {
        if (nuevosPermisos == null) {
            throw new IllegalArgumentException("Los permisos no pueden ser nulos (use lista vacía para limpiar)");
        }
        // Validación de elementos no vacíos
        for (String p : nuevosPermisos) {
            if (p == null || p.isBlank()) {
                throw new IllegalArgumentException("Un permiso no puede ser nulo o vacío");
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

    public List<String> getPermisos() {
        return Collections.unmodifiableList(permisos);
    }
}
