package org.parangaricutirimicuaro.revtech.application.identidad.port.in;

public interface AutenticarUseCase {

    String login(String username, String password);

    boolean validarToken(String token);
}
