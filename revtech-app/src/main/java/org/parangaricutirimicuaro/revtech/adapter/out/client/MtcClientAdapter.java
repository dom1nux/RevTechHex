package org.parangaricutirimicuaro.revtech.adapter.out.client;

import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.MtcPort;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.CondicionInspeccion;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;
import org.springframework.stereotype.Component;

@Component
public class MtcClientAdapter implements MtcPort {

    private final MtcClient client;

    public MtcClientAdapter(MtcClient client) {
        this.client = client;
    }

    @Override
    public void reportarResultado(InspeccionId inspeccion, String placa, CondicionInspeccion condicion) {
        client.validarNormativa(inspeccion.valor(), placa, condicion.name());
    }
}
