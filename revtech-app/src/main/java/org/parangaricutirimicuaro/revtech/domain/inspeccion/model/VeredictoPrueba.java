package org.parangaricutirimicuaro.revtech.domain.inspeccion.model;

/**
 * Veredicto de una prueba técnica. OBSERVADO y RECHAZADO implican deficiencias que impiden el certificado.
 */
public enum VeredictoPrueba {
    APROBADO,
    OBSERVADO,
    RECHAZADO;

    public static VeredictoPrueba desde(String valor) {
        return Catalogo.interpretar(VeredictoPrueba.class, valor, "resultado de la prueba");
    }

    public boolean esDeficiente() {
        return this != APROBADO;
    }
}
