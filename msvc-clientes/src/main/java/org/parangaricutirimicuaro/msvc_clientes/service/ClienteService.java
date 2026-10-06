package org.parangaricutirimicuaro.msvc_clientes.service;

import org.parangaricutirimicuaro.msvc_clientes.model.dto.ClienteResponseDto;

public interface ClienteService {
    ClienteResponseDto buscarPorId(Long idCliente);
    ClienteResponseDto buscarPorDocIdent(String docIdent);
}
