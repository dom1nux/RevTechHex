package org.parangaricutirimicuaro.msvc_inspection.adapter.in.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.msvc_inspection.application.exception.ReferenciaExternaInvalidaException;
import org.parangaricutirimicuaro.msvc_inspection.application.exception.ServicioExternoNoDisponibleException;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.ConsultarInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.CrearInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.CrearInspeccionUseCase.CrearInspeccionCommand;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.FinalizarInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.IniciarInspeccionUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.RegistrarPruebaUseCase;
import org.parangaricutirimicuaro.msvc_inspection.application.port.in.RegistrarPruebaUseCase.RegistrarPruebaCommand;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.InspeccionNoEncontradaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.ReglaNegocioVioladaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.TransicionEstadoInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionFixtures.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InspeccionController.class)
class InspeccionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CrearInspeccionUseCase crearInspeccion;
    @MockitoBean
    private IniciarInspeccionUseCase iniciarInspeccion;
    @MockitoBean
    private RegistrarPruebaUseCase registrarPrueba;
    @MockitoBean
    private FinalizarInspeccionUseCase finalizarInspeccion;
    @MockitoBean
    private ConsultarInspeccionUseCase consultarInspeccion;

    @Test
    @DisplayName("POST /api/inspecciones crea la inspección y responde 201 con el contrato público")
    void crea() throws Exception {
        when(crearInspeccion.crear(any())).thenReturn(registrada());

        mvc.perform(post("/api/inspecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idVehiculo":1,"idInspector":1,"idSupervisor":2,"tipoInspeccion":"INSPECCION"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInspeccion").value(100))
                .andExpect(jsonPath("$.estadoProceso").value("REGISTRADA"))
                .andExpect(jsonPath("$.tipoInspeccion").value("INSPECCION"))
                .andExpect(jsonPath("$.evaluaciones").isEmpty())
                .andExpect(jsonPath("$.periodoInspeccion.fechaInicio").doesNotExist())
                .andExpect(jsonPath("$.resultado").doesNotExist())
                .andExpect(jsonPath("$.certificado").doesNotExist());

        verify(crearInspeccion).crear(new CrearInspeccionCommand(1L, 1L, 2L, "INSPECCION", null));
    }

    @Test
    @DisplayName("POST /api/inspecciones sin vehículo responde 400 sin invocar el caso de uso")
    void creaSinVehiculo() throws Exception {
        mvc.perform(post("/api/inspecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idInspector\":1}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(crearInspeccion);
    }

    @Test
    @DisplayName("PUT /{id}/iniciar delega en el caso de uso")
    void inicia() throws Exception {
        when(iniciarInspeccion.iniciar(100L)).thenReturn(registrada());

        mvc.perform(put("/api/inspecciones/100/iniciar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInspeccion").value(100));
    }

    @Test
    @DisplayName("POST /{id}/pruebas traduce la petición a un comando")
    void registraPrueba() throws Exception {
        when(registrarPrueba.registrar(any())).thenReturn(registrada());

        mvc.perform(post("/api/inspecciones/100/pruebas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prueba\":\"Frenos\",\"resultado\":\"APROBADO\"}"))
                .andExpect(status().isOk());

        verify(registrarPrueba).registrar(new RegistrarPruebaCommand(100L, "Frenos", "APROBADO"));
    }

    @Test
    @DisplayName("POST /{id}/pruebas con campos vacíos responde 400")
    void registraPruebaInvalida() throws Exception {
        mvc.perform(post("/api/inspecciones/100/pruebas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prueba\":\"\",\"resultado\":\"\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(registrarPrueba);
    }

    @Test
    @DisplayName("PUT /{id}/finalizar devuelve resultado, certificado y periodo cerrado")
    void finaliza() throws Exception {
        when(finalizarInspeccion.finalizar(100L, "Sin observaciones")).thenReturn(finalizadaApta());

        mvc.perform(put("/api/inspecciones/100/finalizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"observaciones\":\"Sin observaciones\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoProceso").value("FINALIZADA"))
                .andExpect(jsonPath("$.resultado.condicion").value("APTO"))
                .andExpect(jsonPath("$.evaluaciones[0].prueba").value("FRENOS"))
                .andExpect(jsonPath("$.evaluaciones[0].resultado").value("APROBADO"))
                .andExpect(jsonPath("$.certificado.idCertificado").value(7))
                .andExpect(jsonPath("$.certificado.inspeccionId").value(100))
                .andExpect(jsonPath("$.periodoInspeccion.fechaInicio").value("2026-10-05T09:00:00"))
                .andExpect(jsonPath("$.periodoInspeccion.fechaFin").value("2026-10-05T10:00:00"))
                .andExpect(jsonPath("$.acta").doesNotExist());
    }

    @Test
    @DisplayName("PUT /{id}/finalizar acepta cuerpo vacío")
    void finalizaSinCuerpo() throws Exception {
        when(finalizarInspeccion.finalizar(100L, null)).thenReturn(finalizadaApta());

        mvc.perform(put("/api/inspecciones/100/finalizar"))
                .andExpect(status().isOk());

        verify(finalizarInspeccion).finalizar(100L, null);
    }

    @Test
    @DisplayName("GET /{id} de una inspección inexistente responde 404 con Problem Details")
    void noEncontrada() throws Exception {
        when(consultarInspeccion.obtenerPorId(404L)).thenThrow(new InspeccionNoEncontradaException(404L));

        mvc.perform(get("/api/inspecciones/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Inspección no encontrada"))
                .andExpect(jsonPath("$.detail").value("Inspección técnica no encontrada con ID: 404"));
    }

    @Test
    @DisplayName("Una transición de estado inválida responde 409")
    void transicionInvalida() throws Exception {
        when(iniciarInspeccion.iniciar(100L)).thenThrow(new TransicionEstadoInvalidaException("ya finalizada"));

        mvc.perform(put("/api/inspecciones/100/iniciar"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("ya finalizada"));
    }

    @Test
    @DisplayName("Una regla de negocio violada responde 409")
    void reglaNegocio() throws Exception {
        when(registrarPrueba.registrar(any()))
                .thenThrow(new ReglaNegocioVioladaException("La prueba FRENOS ya fue registrada en esta inspección"));

        mvc.perform(post("/api/inspecciones/100/pruebas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prueba\":\"Frenos\",\"resultado\":\"APROBADO\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Regla de negocio violada"));
    }

    @Test
    @DisplayName("Un dato de dominio inválido responde 400")
    void datoInvalido() throws Exception {
        when(crearInspeccion.crear(any())).thenThrow(new DatoInvalidoException("Tipo de inspección no reconocido: X"));

        mvc.perform(post("/api/inspecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idVehiculo\":1,\"idInspector\":1,\"tipoInspeccion\":\"X\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Tipo de inspección no reconocido: X"));
    }

    @Test
    @DisplayName("Una referencia externa inválida responde 422")
    void referenciaInvalida() throws Exception {
        when(crearInspeccion.crear(any())).thenThrow(new ReferenciaExternaInvalidaException("Vehículo no encontrado con ID: 999"));

        mvc.perform(post("/api/inspecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idVehiculo\":999,\"idInspector\":1}"))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @DisplayName("Un servicio externo caído responde 503")
    void servicioExternoCaido() throws Exception {
        when(crearInspeccion.crear(any())).thenThrow(new ServicioExternoNoDisponibleException("msvc-clientes caído", null));

        mvc.perform(post("/api/inspecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idVehiculo\":1,\"idInspector\":1}"))
                .andExpect(status().isServiceUnavailable());
    }
}
