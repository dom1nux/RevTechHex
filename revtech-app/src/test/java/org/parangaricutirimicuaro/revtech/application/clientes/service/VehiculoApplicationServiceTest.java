package org.parangaricutirimicuaro.revtech.application.clientes.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.CrearVehiculoUseCase.CrearVehiculoCommand;
import org.parangaricutirimicuaro.revtech.application.clientes.port.out.VehiculoRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.clientes.exception.DatoClienteInvalidoException;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.CategoriaVehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.ClienteId;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.Vehiculo;
import org.parangaricutirimicuaro.revtech.domain.clientes.model.VehiculoId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VehiculoApplicationServiceTest {

    private final EnMemoria repository = new EnMemoria();
    private final VehiculoApplicationService service = new VehiculoApplicationService(repository);

    private Vehiculo crear(Long clienteId, String placa, CategoriaVehiculo categoria) {
        return service.crear(new CrearVehiculoCommand(clienteId, placa, categoria, "Toyota", "Corolla", 2021));
    }

    @Test
    @DisplayName("Crea el vehículo normalizando la placa y lo asigna al cliente")
    void crearVehiculo() {
        Vehiculo creado = crear(20L, " abc-999 ", CategoriaVehiculo.M1);

        assertThat(creado.getId()).isNotNull();
        assertThat(creado.getPlaca()).isEqualTo("ABC-999");
        assertThat(creado.getPropietario()).isEqualTo(ClienteId.of(20L));
    }

    @Test
    @DisplayName("Rechaza un vehículo sin placa")
    void crearSinPlaca() {
        assertThatThrownBy(() -> crear(20L, " ", CategoriaVehiculo.M1))
                .isInstanceOf(DatoClienteInvalidoException.class);
    }

    @Test
    @DisplayName("Busca por ID y por placa; un vehículo inexistente devuelve vacío")
    void buscar() {
        Vehiculo creado = crear(10L, "XYZ-789", CategoriaVehiculo.N1);

        assertThat(service.buscarPorId(creado.getId().valor())).contains(creado);
        assertThat(service.buscarPorPlaca("XYZ-789")).contains(creado);
        assertThat(service.buscarPorId(99L)).isEmpty();
    }

    @Test
    @DisplayName("Lista los vehículos de un cliente")
    void listarPorCliente() {
        Vehiculo propio = crear(5L, "P1A-100", CategoriaVehiculo.M1);
        crear(6L, "P1A-200", CategoriaVehiculo.M1);

        assertThat(service.listarPorCliente(5L)).containsExactly(propio);
    }

    private static final class EnMemoria implements VehiculoRepositoryPort {

        private final List<Vehiculo> vehiculos = new ArrayList<>();

        @Override
        public Vehiculo guardar(Vehiculo v) {
            Vehiculo guardado = Vehiculo.reconstituir(VehiculoId.of(vehiculos.size() + 1L), v.getPropietario(),
                    v.getPlaca(), v.getCategoria(), v.getMarca(), v.getModelo(), v.getAnioFabricacion());
            vehiculos.add(guardado);
            return guardado;
        }

        @Override
        public Optional<Vehiculo> buscarPorId(VehiculoId id) {
            return vehiculos.stream().filter(v -> v.getId().equals(id)).findFirst();
        }

        @Override
        public Optional<Vehiculo> buscarPorPlaca(String placa) {
            return vehiculos.stream().filter(v -> v.getPlaca().equals(placa)).findFirst();
        }

        @Override
        public List<Vehiculo> listarPorPropietario(ClienteId propietario) {
            return vehiculos.stream().filter(v -> v.getPropietario().equals(propietario)).toList();
        }
    }
}
