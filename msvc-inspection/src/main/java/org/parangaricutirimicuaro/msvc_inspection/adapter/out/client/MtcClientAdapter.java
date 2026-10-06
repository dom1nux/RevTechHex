package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client;

import org.parangaricutirimicuaro.msvc_inspection.application.port.out.MtcPort;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.CondicionInspeccion;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionId;
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
