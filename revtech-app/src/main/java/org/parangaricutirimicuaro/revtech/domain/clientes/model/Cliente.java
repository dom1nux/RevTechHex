package org.parangaricutirimicuaro.revtech.domain.clientes.model;

import org.parangaricutirimicuaro.revtech.domain.clientes.exception.DatoClienteInvalidoException;

import java.time.LocalDateTime;

/**
 * Propietario o conductor atendido por RevTech.
 */
public class Cliente {

    private final ClienteId id;
    private final String docIdent;
    private final String nombre;
    private final LocalDateTime fechaRegistro;

    private Cliente(ClienteId id, String docIdent, String nombre, LocalDateTime fechaRegistro) {
        if (docIdent == null || docIdent.isBlank()) {
            throw new DatoClienteInvalidoException("El documento de identidad no puede ser nulo o vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatoClienteInvalidoException("El nombre no puede ser nulo o vacío");
        }
        if (fechaRegistro == null) {
            throw new DatoClienteInvalidoException("La fecha de registro es obligatoria");
        }
        this.id = id;
        this.docIdent = docIdent.trim();
        this.nombre = nombre.trim();
        this.fechaRegistro = fechaRegistro;
    }

    public static Cliente registrar(String docIdent, String nombre, LocalDateTime ahora) {
        return new Cliente(null, docIdent, nombre, ahora);
    }

    /**
     * Recrea un cliente ya persistido.
     */
    public static Cliente reconstituir(ClienteId id, String docIdent, String nombre, LocalDateTime fechaRegistro) {
        if (id == null) {
            throw new IllegalArgumentException("Solo se puede reconstituir un cliente ya persistido");
        }
        return new Cliente(id, docIdent, nombre, fechaRegistro);
    }

    public ClienteId getId() {
        return id;
    }

    public String getDocIdent() {
        return docIdent;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
