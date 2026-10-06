package org.parangaricutirimicuaro.revtech.adapter.out.client.feign;

import org.parangaricutirimicuaro.revtech.adapter.out.client.UsuarioClient;
import org.parangaricutirimicuaro.revtech.adapter.out.client.dto.UsuarioResponseDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-identidad", url = "${clients.msvc-identidad.url:http://localhost:8082}")
@ConditionalOnProperty(name = "revtech.clients.mock", havingValue = "false")
public interface FeignUsuarioClient extends UsuarioClient {

    @Override
    @GetMapping("/api/usuarios/{idUsuario}")
    UsuarioResponseDto obtenerUsuarioPorId(@PathVariable("idUsuario") Long idUsuario);
}
