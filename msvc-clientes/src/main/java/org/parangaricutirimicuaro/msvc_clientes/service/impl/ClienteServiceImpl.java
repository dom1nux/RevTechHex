package org.parangaricutirimicuaro.msvc_clientes.service.impl;

import org.parangaricutirimicuaro.msvc_clientes.model.dto.ClienteResponseDto;
import org.parangaricutirimicuaro.msvc_clientes.repository.ClienteRepository;
import org.parangaricutirimicuaro.msvc_clientes.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteResponseDto buscarPorId(Long idCliente) {
        return clienteRepository.findById(idCliente)
                .map(ClienteResponseDto::fromEntity)
                .orElse(null);
    }

    @Override
    public ClienteResponseDto buscarPorDocIdent(String docIdent) {
        return clienteRepository.findByDocIdent(docIdent)
                .map(ClienteResponseDto::fromEntity)
                .orElse(null);
    }
}
