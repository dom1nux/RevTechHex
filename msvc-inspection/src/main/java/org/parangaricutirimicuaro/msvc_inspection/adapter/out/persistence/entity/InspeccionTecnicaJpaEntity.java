package org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.CondicionInspeccion;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.EstadoProceso;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.TipoInspeccion;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "inspecciones_tecnicas")
public class InspeccionTecnicaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inspeccion")
    private Long idInspeccion;

    @Column(name = "vehiculo_id", nullable = false)
    private Long idVehiculo;

    @Column(name = "inspector_id")
    private Long idInspector;

    @Column(name = "supervisor_id")
    private Long idSupervisor;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "tipo_inspeccion", length = 30)
    private TipoInspeccion tipoInspeccion;

    @Column(name = "inspeccion_origen_id")
    private Long idInspeccionOrigen;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "estado_proceso", length = 30)
    private EstadoProceso estadoProceso;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Embedded
    private PeriodoInspeccionEmbeddable periodoInspeccion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "resultado_condicion", length = 30)
    private CondicionInspeccion resultadoCondicion;

    @ElementCollection
    @CollectionTable(name = "inspeccion_pruebas", joinColumns = @JoinColumn(name = "inspeccion_id"))
    private List<ResultadoPruebaEmbeddable> evaluaciones = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "certificado_id", referencedColumnName = "id_certificado")
    private CertificadoInspeccionJpaEntity certificado;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "acta_id", referencedColumnName = "id_acta")
    private ActaObservacionesJpaEntity acta;
}
