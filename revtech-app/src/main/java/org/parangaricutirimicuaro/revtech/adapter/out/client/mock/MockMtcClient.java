package org.parangaricutirimicuaro.revtech.adapter.out.client.mock;

import org.parangaricutirimicuaro.revtech.adapter.out.client.MtcClient;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.MtcValidacionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "revtech.clients.mock", havingValue = "true", matchIfMissing = true)
public class MockMtcClient implements MtcClient {

    private static final Logger log = LoggerFactory.getLogger(MockMtcClient.class);

    @Override
    public MtcValidacionResponseDto validarNormativa(Long idInspeccion, String placa, String resultado) {
        log.info("[MOCK-MTC] Notificando inspección {} para placa {} con resultado {}", idInspeccion, placa, resultado);

        String codigoValidacion = "MTC-REV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return new MtcValidacionResponseDto(
                true,
                codigoValidacion,
                "Inspección vehicular homologada y registrada satisfactoriamente en MTC (Simulado)"
        );
    }
}
