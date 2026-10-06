package org.parangaricutirimicuaro.msvc_inspection.adapter.out.client;

import feign.FeignException;
import org.parangaricutirimicuaro.msvc_inspection.application.exception.ServicioExternoNoDisponibleException;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Normaliza las respuestas de servicios externos: un recurso inexistente (null o HTTP 404) se traduce
 * en {@link Optional#empty()} y cualquier otro fallo en {@link ServicioExternoNoDisponibleException}.
 */
final class ExternalCalls {

    private ExternalCalls() {
    }

    static <T> Optional<T> consultar(String servicio, Supplier<T> llamada) {
        try {
            return Optional.ofNullable(llamada.get());
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (RuntimeException e) {
            throw new ServicioExternoNoDisponibleException(
                    "No fue posible consultar " + servicio + ": " + e.getMessage(), e);
        }
    }
}
