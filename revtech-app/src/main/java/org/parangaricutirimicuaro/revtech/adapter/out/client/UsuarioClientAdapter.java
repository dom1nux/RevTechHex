package org.parangaricutirimicuaro.revtech.adapter.out.client;

import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.UsuarioPort;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PersonalId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioClientAdapter implements UsuarioPort {

    private final UsuarioClient client;

    public UsuarioClientAdapter(UsuarioClient client) {
        this.client = client;
    }

    @Override
    public Optional<UsuarioInfo> buscarPorId(PersonalId id) {
        return ExternalCalls.consultar("msvc-identidad", () -> client.obtenerUsuarioPorId(id.valor()))
                .map(dto -> new UsuarioInfo(id, dto.nombreRol(), Boolean.TRUE.equals(dto.estadoActivo())));
    }
}
