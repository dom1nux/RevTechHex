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
@Table(name = "actas_observaciones")
public class ActaObservacionesJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acta")
    private Long idActa;

    @Column(name = "inspeccion_id")
    private Long inspeccionId;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;
}
