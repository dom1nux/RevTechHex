package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client;

import org.parangaricutirimicuaro.msvc_inspection.application.port.out.UsuarioPort;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.PersonalId;
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
