package org.parangaricutirimicuaro.msvc_inspection.adapter.in.web;

import org.parangaricutirimicuaro.msvc_inspection.application.exception.ReferenciaExternaInvalidaException;
import org.parangaricutirimicuaro.msvc_inspection.application.exception.ServicioExternoNoDisponibleException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.DatoInvalidoException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.InspeccionNoEncontradaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.ReglaNegocioVioladaException;
import org.parangaricutirimicuaro.msvc_inspection.domain.exception.TransicionEstadoInvalidaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones del dominio y de la aplicación a respuestas HTTP con formato RFC 9457 (Problem Details).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InspeccionNoEncontradaException.class)
    ProblemDetail handleNoEncontrada(InspeccionNoEncontradaException e) {
        return problem(HttpStatus.NOT_FOUND, "Inspección no encontrada", e.getMessage());
    }

    @ExceptionHandler(DatoInvalidoException.class)
    ProblemDetail handleDatoInvalido(DatoInvalidoException e) {
        return problem(HttpStatus.BAD_REQUEST, "Dato inválido", e.getMessage());
    }

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    ProblemDetail handleTransicionInvalida(TransicionEstadoInvalidaException e) {
        return problem(HttpStatus.CONFLICT, "Operación no permitida en el estado actual", e.getMessage());
    }

    @ExceptionHandler(ReglaNegocioVioladaException.class)
    ProblemDetail handleReglaNegocio(ReglaNegocioVioladaException e) {
        return problem(HttpStatus.CONFLICT, "Regla de negocio violada", e.getMessage());
    }

    @ExceptionHandler(ReferenciaExternaInvalidaException.class)
    ProblemDetail handleReferenciaInvalida(ReferenciaExternaInvalidaException e) {
        return problem(HttpStatus.UNPROCESSABLE_CONTENT, "Referencia externa inválida", e.getMessage());
    }

    @ExceptionHandler(ServicioExternoNoDisponibleException.class)
    ProblemDetail handleServicioExterno(ServicioExternoNoDisponibleException e) {
        log.error("Fallo de integración con servicio externo", e);
        return problem(HttpStatus.SERVICE_UNAVAILABLE, "Servicio externo no disponible", e.getMessage());
    }

    private static ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return problem;
    }
}
