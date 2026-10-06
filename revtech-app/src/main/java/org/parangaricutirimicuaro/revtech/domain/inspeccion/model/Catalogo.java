package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

import org.parangaricutirimicuaro.revtech.domain.inspeccion.exception.DatoInvalidoException;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Interpreta valores de catálogo recibidos como texto, sin distinguir mayúsculas, tildes ni espacios.
 */
final class Catalogo {

    private Catalogo() {
    }

    static <E extends Enum<E>> E interpretar(Class<E> tipo, String valor, String concepto) {
        if (valor == null || valor.isBlank()) {
            throw new DatoInvalidoException("El " + concepto + " es obligatorio");
        }
        String normalizado = Normalizer.normalize(valor.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[\\s-]+", "_")
                .toUpperCase(Locale.ROOT);
        try {
            return Enum.valueOf(tipo, normalizado);
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException(concepto.substring(0, 1).toUpperCase(Locale.ROOT) + concepto.substring(1)
                    + " no reconocido: " + valor);
        }
    }
}
