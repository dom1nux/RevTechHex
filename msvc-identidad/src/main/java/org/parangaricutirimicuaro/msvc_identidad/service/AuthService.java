package org.parangaricutirimicuaro.msvc_identidad.service;

import org.parangaricutirimicuaro.msvc_identidad.model.dto.LoginRequestDto;

public interface AuthService {

    String login(LoginRequestDto dto);
    boolean validarToken(String token);
}
