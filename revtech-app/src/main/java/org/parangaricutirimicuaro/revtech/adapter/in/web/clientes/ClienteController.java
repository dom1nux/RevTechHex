package org.parangaricutirimicuaro.revtech.adapter.in.web.clientes;

import org.parangaricutirimicuaro.revtech.adapter.in.web.clientes.dto.ClienteResponseDto;
import org.parangaricutirimicuaro.revtech.application.clientes.port.in.ConsultarClienteUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada: expone los casos de uso de clientes en {@code /api/clientes}.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ConsultarClienteUseCase consultarCliente;

    public ClienteController(ConsultarClienteUseCase consultarCliente) {
        this.consultarCliente = consultarCliente;
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
