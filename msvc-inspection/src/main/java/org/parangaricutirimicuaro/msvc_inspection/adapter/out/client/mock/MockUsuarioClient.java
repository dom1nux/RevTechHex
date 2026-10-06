package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.mock;

import org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.UsuarioClient;
import org.parangaricutirimicuaro.msvc_inspection.adapter.out.client.dto.UsuarioResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "revtech.clients.mock", havingValue = "true", matchIfMissing = true)
public class MockUsuarioClient implements UsuarioClient {

    private static final Logger log = LoggerFactory.getLogger(MockUsuarioClient.class);

    @Override
    public UsuarioResponseDto obtenerUsuarioPorId(Long idUsuario) {
        log.info("[MOCK] Consultando usuario con id: {}", idUsuario);

        if (idUsuario == null) {
            throw new IllegalArgumentException("El id del usuario no puede ser nulo");
        }

        if (idUsuario == 999L) {
            log.info("[MOCK] Usuario {} no existe en msvc-identidad (simulado)", idUsuario);
            return null;
        }

        String rol = (idUsuario % 2 == 0) ? "SUPERVISOR" : "INSPECTOR";
        String username = rol.toLowerCase() + "_" + idUsuario;

        return new UsuarioResponseDto(
                idUsuario,
                username,
                rol,
                true
        );
    }
}
