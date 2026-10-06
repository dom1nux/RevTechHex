package org.parangaricutirimicuaro.msvc_clientes.controller;

import org.parangaricutirimicuaro.msvc_clientes.model.dto.ClienteResponseDto;
import org.parangaricutirimicuaro.msvc_clientes.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDto> obtenerPorId(@PathVariable("idCliente") Long idCliente) {
        ClienteResponseDto dto = clienteService.buscarPorId(idCliente);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/doc/{docIdent}")
    public ResponseEntity<ClienteResponseDto> obtenerPorDocIdent(@PathVariable("docIdent") String docIdent) {
        ClienteResponseDto dto = clienteService.buscarPorDocIdent(docIdent);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
