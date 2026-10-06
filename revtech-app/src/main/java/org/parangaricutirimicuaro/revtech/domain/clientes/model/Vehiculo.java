package org.parangaricutirimicuaro.revtech.domain.clientes.model;

import org.parangaricutirimicuaro.revtech.domain.clientes.exception.DatoClienteInvalidoException;

/**
 * Vehículo registrado a nombre de un cliente, referenciado únicamente por su identificador.
 */
public class Vehiculo {

    private final VehiculoId id;
    private ClienteId propietario;
    private final String placa;
    private final CategoriaVehiculo categoria;
    private final String marca;
    private final String modelo;
    private final Integer anioFabricacion;

    private Vehiculo(VehiculoId id, ClienteId propietario, String placa, CategoriaVehiculo categoria,
                     String marca, String modelo, Integer anioFabricacion) {
        if (propietario == null) {
            throw new DatoClienteInvalidoException("clienteId no puede ser nulo");
        }
        if (placa == null || placa.isBlank()) {
            throw new DatoClienteInvalidoException("La placa no puede ser nula o vacía");
        }
        this.id = id;
        this.propietario = propietario;
        this.placa = placa.trim().toUpperCase();
        this.categoria = categoria;
        this.marca = marca;
        this.modelo = modelo;
        this.anioFabricacion = anioFabricacion;
    }

    public static Vehiculo registrar(ClienteId propietario, String placa, CategoriaVehiculo categoria,
                                     String marca, String modelo, Integer anioFabricacion) {
        return new Vehiculo(null, propietario, placa, categoria, marca, modelo, anioFabricacion);
    }

    /**
     * Recrea un vehículo ya persistido.
     */
    public static Vehiculo reconstituir(VehiculoId id, ClienteId propietario, String placa, CategoriaVehiculo categoria,
                                        String marca, String modelo, Integer anioFabricacion) {
        if (id == null) {
            throw new IllegalArgumentException("Solo se puede reconstituir un vehículo ya persistido");
        }
        return new Vehiculo(id, propietario, placa, categoria, marca, modelo, anioFabricacion);
    }

    public void transferirPropietario(ClienteId nuevoPropietario) {
        if (nuevoPropietario == null) {
            throw new DatoClienteInvalidoException("nuevoClienteId no puede ser nulo");
        }
        this.propietario = nuevoPropietario;
    }

    public VehiculoId getId() {
        return id;
    }

    public ClienteId getPropietario() {
        return propietario;
    }

    public String getPlaca() {
        return placa;
    }

    public CategoriaVehiculo getCategoria() {
        return categoria;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAnioFabricacion() {
        return anioFabricacion;
    }
}
