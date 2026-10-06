package org.parangaricutirimicuaro.msvc_clientes.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.parangaricutirimicuaro.msvc_clientes.model.type.CategoriaVehiculo;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "vehiculos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehiculo_placa", columnNames = "placa")
})
public class Vehiculo {

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

    public Vehiculo(Long clienteId, String placa, CategoriaVehiculo categoria, String marca, String modelo, Integer anioFabricacion) {
        if (clienteId == null) {
            throw new IllegalArgumentException("clienteId no puede ser nulo");
        }
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa no puede ser nula o vacía");
        }
        this.clienteId = clienteId;
        this.placa = placa.trim().toUpperCase();
        this.categoria = categoria;
        this.marca = marca;
        this.modelo = modelo;
        this.anioFabricacion = anioFabricacion;
    }

    public void transferirPropietario(Long nuevoClienteId) {
        if (nuevoClienteId == null) {
            throw new IllegalArgumentException("nuevoClienteId no puede ser nulo");
        }
        this.clienteId = nuevoClienteId;
    }
}
