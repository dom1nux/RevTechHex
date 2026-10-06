package org.parangaricutirimicuaro.revtech.adapter.in.web.identidad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarRolUseCase;
import org.parangaricutirimicuaro.revtech.application.identidad.port.in.RegistrarRolUseCase.RegistrarRolCommand;
import org.parangaricutirimicuaro.revtech.domain.identidad.exception.ReglaIdentidadVioladaException;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RolController.class)
class RolControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RegistrarRolUseCase registrarRol;

    @Test
    @DisplayName("POST /api/roles registra el rol y responde 201")
    void registra() throws Exception {
        when(registrarRol.registrar(any())).thenReturn(
                RolAcceso.reconstituir(RolId.of(3L), NombreRol.ROLE_INSPECTOR, List.of("a"), true));

        mvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreRol":"ROLE_INSPECTOR","permisos":["a"]}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRol").value(3))
                .andExpect(jsonPath("$.nombreRol").value("ROLE_INSPECTOR"))
                .andExpect(jsonPath("$.estadoActivo").value(true));

        verify(registrarRol).registrar(new RegistrarRolCommand(NombreRol.ROLE_INSPECTOR, List.of("a")));
    }

    @Test
    @DisplayName("POST /api/roles sin nombreRol responde 400")
    void sinNombre() throws Exception {
        mvc.perform(post("/api/roles").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/roles con rol duplicado responde 409")
    void duplicado() throws Exception {
        when(registrarRol.registrar(any())).thenThrow(new ReglaIdentidadVioladaException("duplicado"));

        mvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreRol":"ROLE_ADMIN"}
                                """))
                .andExpect(status().isConflict());
    }
}
