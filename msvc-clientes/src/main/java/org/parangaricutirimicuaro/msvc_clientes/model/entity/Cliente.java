package org.parangaricutirimicuaro.msvc_clientes.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "clientes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cliente_doc_ident", columnNames = "doc_ident")
})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "doc_ident", nullable = false, length = 20)
    private String docIdent;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    public Cliente(String docIdent, String nombre) {
        if (docIdent == null || docIdent.isBlank()) {
            throw new IllegalArgumentException("El documento de identidad no puede ser nulo o vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        this.docIdent = docIdent.trim();
        this.nombre = nombre.trim();
        this.fechaRegistro = LocalDateTime.now();
    }
}
