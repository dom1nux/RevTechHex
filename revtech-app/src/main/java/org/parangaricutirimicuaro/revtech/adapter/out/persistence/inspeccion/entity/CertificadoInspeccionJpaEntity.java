package org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "certificados_inspeccion")
public class CertificadoInspeccionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Long idCertificado;

    @Column(name = "inspeccion_id")
    private Long inspeccionId;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;
}
