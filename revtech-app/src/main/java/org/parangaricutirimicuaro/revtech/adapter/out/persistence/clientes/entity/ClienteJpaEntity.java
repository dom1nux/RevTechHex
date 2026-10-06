package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clientes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cliente_doc_ident", columnNames = "doc_ident")
})
public class ClienteJpaEntity {

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
}
