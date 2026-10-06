package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles_acceso", uniqueConstraints = {
        @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombre_rol")
})
public class RolAccesoJpaEntity {

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
    private boolean estadoActivo;
}
