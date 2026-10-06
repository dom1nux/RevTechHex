package org.parangaricutirimicuaro.msvc_identidad.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_username", columnNames = "username")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255, name = "password_hash")
    private String passwordHash;

    @Column(name = "rol_activo_id")
    private Long rolActivoId;

    @Column(nullable = false, name = "estado_activo")
    private boolean estadoActivo = true;

    public Usuario(String username, String passwordHash, Long rolActivoId) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username no puede ser nulo o vacío");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash no puede ser nulo o vacío");
        }
        this.username = username;
        this.passwordHash = passwordHash;
        this.rolActivoId = rolActivoId;
        this.estadoActivo = true;
    }

    public void cambiarContrasena(String nuevoHash) {
        if (nuevoHash == null || nuevoHash.isBlank()) {
            throw new IllegalArgumentException("El nuevo hash no puede ser nulo o vacío");
        }
        this.passwordHash = nuevoHash;
    }

    public void cambiarEstadoActivo(boolean estado) {
        this.estadoActivo = estado;
    }

    public void asignarRol(Long rolId) {
        if (rolId == null) {
            throw new IllegalArgumentException("rolId no puede ser nulo");
        }
        this.rolActivoId = rolId;
    }
}
