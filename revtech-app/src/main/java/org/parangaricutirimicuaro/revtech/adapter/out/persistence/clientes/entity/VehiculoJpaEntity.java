package org.parangaricutirimicuaro.revtech.adapter.out.persistence.clientes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.CategoriaVehiculo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vehiculos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehiculo_placa", columnNames = "placa")
})
public class VehiculoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(nullable = false, length = 10)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CategoriaVehiculo categoria;

    @Column(length = 50)
    private String marca;

    @Column(length = 50)
    private String modelo;

    @Column(name = "anio_fabricacion")
    private Integer anioFabricacion;
}
