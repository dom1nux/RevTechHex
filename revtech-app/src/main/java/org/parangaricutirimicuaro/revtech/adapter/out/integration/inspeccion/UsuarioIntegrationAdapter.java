package org.parangaricutirimicuaro.revtech.adapter.out.integration.inspeccion;

import org.parangaricutirimicuaro.revtech.application.identidad.port.in.ConsultarUsuarioUseCase;
import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.UsuarioPort;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.PersonalId;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Puente en proceso hacia Identidad y Acceso: traduce su modelo a la vista mínima que necesita Inspección.
 */
@Component
public class UsuarioIntegrationAdapter implements UsuarioPort {

    private final ConsultarUsuarioUseCase consultarUsuario;

    public UsuarioIntegrationAdapter(ConsultarUsuarioUseCase consultarUsuario) {
        this.consultarUsuario = consultarUsuario;
    }

    @Override
    public Optional<UsuarioInfo> buscarPorId(PersonalId id) {
        return consultarUsuario.buscarPorId(id.valor())
                .map(detalle -> new UsuarioInfo(id,
                        detalle.nombreRol() != null ? detalle.nombreRol().name() : null,
                        detalle.usuario().isEstadoActivo()));
    }
}
