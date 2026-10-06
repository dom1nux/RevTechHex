package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

/**
 * Pruebas técnicas que contempla el proceso de RevTech (Diseño Táctico, objeto de valor ResultadoPrueba).
 */
public enum TipoPrueba {
    FRENOS,
    DIRECCION,
    SUSPENSION,
    LUCES,
    NEUMATICOS,
    EMISIONES;

    public static TipoPrueba desde(String valor) {
        return Catalogo.interpretar(TipoPrueba.class, valor, "tipo de prueba");
    }
}
