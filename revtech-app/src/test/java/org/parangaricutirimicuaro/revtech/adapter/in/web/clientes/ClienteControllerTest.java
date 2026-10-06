package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarClienteUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.RegistrarClienteUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.RegistrarClienteUseCase.RegistrarClienteCommand;
import org.parangaricutirimicuaro.revtech.domain.clientes.exception.ReglaClienteVioladaException;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RegistrarClienteUseCase registrarCliente;
    @MockitoBean
    private ConsultarClienteUseCase consultarCliente;

    @Test
    @DisplayName("POST /api/clientes registra el cliente y responde 201")
    void registra() throws Exception {
        when(registrarCliente.registrar(any())).thenReturn(Cliente.reconstituir(
                ClienteId.of(7L), "12345678", "Ana Torres", LocalDateTime.of(2026, 1, 15, 10, 0)));

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"docIdent":"12345678","nombre":"Ana Torres"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCliente").value(7))
                .andExpect(jsonPath("$.docIdent").value("12345678"))
                .andExpect(jsonPath("$.nombre").value("Ana Torres"));

        verify(registrarCliente).registrar(new RegistrarClienteCommand("12345678", "Ana Torres"));
    }

    @Test
    @DisplayName("POST /api/clientes sin nombre responde 400")
    void sinNombre() throws Exception {
        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"docIdent":"12345678"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/clientes con documento duplicado responde 409")
    void duplicado() throws Exception {
        when(registrarCliente.registrar(any())).thenThrow(new ReglaClienteVioladaException("duplicado"));

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"docIdent":"12345678","nombre":"Ana Torres"}
                                """))
                .andExpect(status().isConflict());
    }
}
