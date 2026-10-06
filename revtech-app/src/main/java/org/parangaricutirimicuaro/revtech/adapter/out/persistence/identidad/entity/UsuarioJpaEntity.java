package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_username", columnNames = "username")
})
public class UsuarioJpaEntity {

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
    private boolean estadoActivo;
}
