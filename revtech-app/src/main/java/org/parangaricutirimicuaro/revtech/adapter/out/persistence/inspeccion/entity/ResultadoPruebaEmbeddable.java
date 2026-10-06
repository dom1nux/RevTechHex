package org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ResultadoPruebaEmbeddable {

    @Column(name = "prueba", nullable = false, length = 100)
    private String prueba;

    @Column(name = "resultado", nullable = false, length = 100)
    private String resultado;
}
