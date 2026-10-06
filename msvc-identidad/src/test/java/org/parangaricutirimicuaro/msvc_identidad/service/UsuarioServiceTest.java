package org.parangaricutirimicuaro.msvc_identidad.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.parangaricutirimicuaro.msvc_identidad.model.dto.UsuarioResponseDto;
import org.parangaricutirimicuaro.msvc_identidad.model.entity.RolAcceso;
import org.parangaricutirimicuaro.msvc_identidad.model.entity.Usuario;
import org.parangaricutirimicuaro.msvc_identidad.model.type.NombreRol;
import org.parangaricutirimicuaro.msvc_identidad.repository.RolAccesoRepository;
import org.parangaricutirimicuaro.msvc_identidad.repository.UsuarioRepository;
import org.parangaricutirimicuaro.msvc_identidad.service.impl.UsuarioServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolAccesoRepository rolAccesoRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    @DisplayName("Debe buscar usuario existente por ID y resolver su rol")
    void buscarPorId_existente_retornaDtoConRol() {
        Usuario usuario = new Usuario("inspector_juan", "hash123", 2L);
        RolAcceso rol = new RolAcceso(NombreRol.ROLE_INSPECTOR, List.of("REGISTRAR_PRUEBA"));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolAccesoRepository.findById(2L)).thenReturn(Optional.of(rol));

        UsuarioResponseDto resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("inspector_juan", resultado.username());
        assertEquals("ROLE_INSPECTOR", resultado.nombreRol());
        assertTrue(resultado.estadoActivo());
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException si el usuario no existe")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.buscarPorId(999L));
    }
}
