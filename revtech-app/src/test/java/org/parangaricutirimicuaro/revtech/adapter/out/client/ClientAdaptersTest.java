package org.parangaricutirimicuaro.revtech.adapter.out.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CondicionInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientAdaptersTest {

    @Mock
    private MtcClient mtcClient;

    @Test
    @DisplayName("Reporta la condición al MTC con su nombre canónico")
    void reportaAlMtc() {
        new MtcClientAdapter(mtcClient).reportarResultado(InspeccionId.of(100L), "ABC-123", CondicionInspeccion.OBSERVADO);

        verify(mtcClient).validarNormativa(100L, "ABC-123", "OBSERVADO");
    }
}
