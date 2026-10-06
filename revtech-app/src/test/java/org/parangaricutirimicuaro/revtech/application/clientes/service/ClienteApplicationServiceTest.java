package org.parangaricutirimicuaro.revtech.application.clientes.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.RegistrarClienteUseCase.RegistrarClienteCommand;
import org.parangaricutirimicuaro.revtech.application.clientes.port.out.ClienteRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.clientes.exception.DatoClienteInvalidoException;
import org.parangaricutirimicuaro.revtech.domain.clientes.exception.ReglaClienteVioladaException;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Cliente;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClienteApplicationServiceTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-01-15T10:00:00Z"), ZoneOffset.UTC);
    private final EnMemoria repository = new EnMemoria();
    private final ClienteApplicationService service = new ClienteApplicationService(repository, clock);

    @Test
    @DisplayName("Registra el cliente con la fecha del reloj y le asigna un id")
    void registra() {
        Cliente creado = service.registrar(new RegistrarClienteCommand(" 12345678 ", " Ana Torres "));

        assertThat(creado.getId()).isNotNull();
        assertThat(creado.getDocIdent()).isEqualTo("12345678");
        assertThat(creado.getNombre()).isEqualTo("Ana Torres");
        assertThat(creado.getFechaRegistro()).isEqualTo(LocalDateTime.of(2026, 1, 15, 10, 0));
        assertThat(service.buscarPorDocIdent("12345678")).contains(creado);
    }

    @Test
    @DisplayName("Rechaza un documento de identidad ya registrado")
    void documentoDuplicado() {
        service.registrar(new RegistrarClienteCommand("12345678", "Ana Torres"));

        assertThatThrownBy(() -> service.registrar(new RegistrarClienteCommand(" 12345678", "Otra Persona")))
                .isInstanceOf(ReglaClienteVioladaException.class);
    }

    @Test
    @DisplayName("Rechaza un cliente sin documento o sin nombre")
    void datosInvalidos() {
        assertThatThrownBy(() -> service.registrar(new RegistrarClienteCommand(" ", "Ana")))
                .isInstanceOf(DatoClienteInvalidoException.class);
        assertThatThrownBy(() -> service.registrar(new RegistrarClienteCommand("1", null)))
                .isInstanceOf(DatoClienteInvalidoException.class);
    }

    private static class EnMemoria implements ClienteRepositoryPort {
        private final Map<Long, Cliente> datos = new HashMap<>();
        private long secuencia = 0;

        @Override
        public Cliente guardar(Cliente cliente) {
            Cliente guardado = Cliente.reconstituir(ClienteId.of(++secuencia), cliente.getDocIdent(),
                    cliente.getNombre(), cliente.getFechaRegistro());
            datos.put(secuencia, guardado);
            return guardado;
        }

        @Override
        public Optional<Cliente> buscarPorId(ClienteId id) {
            return Optional.ofNullable(datos.get(id.valor()));
        }

        @Override
        public Optional<Cliente> buscarPorDocIdent(String docIdent) {
            return datos.values().stream().filter(c -> c.getDocIdent().equals(docIdent)).findFirst();
        }
    }
}
