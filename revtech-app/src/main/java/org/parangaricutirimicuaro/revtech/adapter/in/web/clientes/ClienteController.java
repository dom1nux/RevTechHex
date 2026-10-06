package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes;

import jakarta.validation.Valid;
import org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto.ClienteResponseDto;
import org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto.RegistrarClienteRequestDto;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarClienteUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.RegistrarClienteUseCase;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.RegistrarClienteUseCase.RegistrarClienteCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada: expone los casos de uso de clientes en {@code /api/clientes}.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final RegistrarClienteUseCase registrarCliente;
    private final ConsultarClienteUseCase consultarCliente;

    public ClienteController(RegistrarClienteUseCase registrarCliente, ConsultarClienteUseCase consultarCliente) {
        this.registrarCliente = registrarCliente;
        this.consultarCliente = consultarCliente;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> registrar(@Valid @RequestBody RegistrarClienteRequestDto request) {
        var command = new RegistrarClienteCommand(request.docIdent(), request.nombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClienteResponseDto.from(registrarCliente.registrar(command)));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDto> obtenerPorId(@PathVariable("idCliente") Long idCliente) {
        return ResponseEntity.of(consultarCliente.buscarPorId(idCliente).map(ClienteResponseDto::from));
    }

    @GetMapping("/doc/{docIdent}")
    public ResponseEntity<ClienteResponseDto> obtenerPorDocIdent(@PathVariable("docIdent") String docIdent) {
        return ResponseEntity.of(consultarCliente.buscarPorDocIdent(docIdent).map(ClienteResponseDto::from));
    }
}
