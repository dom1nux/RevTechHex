package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad;

import org.parangaricutirimicuaro.revtech.application.identidad.port.out.RolAccesoRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.NombreRol;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolAcceso;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.RolId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador de salida: implementa {@code RolAccesoRepositoryPort} con JPA.
 */
@Component
@Transactional(readOnly = true)
public class RolAccesoPersistenceAdapter implements RolAccesoRepositoryPort {

    private final SpringDataRolAccesoRepository repository;

    RolAccesoPersistenceAdapter(SpringDataRolAccesoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public RolAcceso guardar(RolAcceso rol) {
        return IdentidadPersistenceMapper.toDomain(repository.save(IdentidadPersistenceMapper.toEntity(rol)));
    }

    @Override
    public boolean existePorNombre(NombreRol nombreRol) {
        return repository.existsByNombreRol(nombreRol);
    }

    @Override
    public Optional<RolAcceso> buscarPorId(RolId id) {
        return repository.findById(id.valor()).map(IdentidadPersistenceMapper::toDomain);
    }
}
