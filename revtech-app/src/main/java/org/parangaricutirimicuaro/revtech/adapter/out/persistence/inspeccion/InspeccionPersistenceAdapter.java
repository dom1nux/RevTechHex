package org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion;

import org.parangaricutirimicuaro.revtech.application.inspeccion.port.out.InspeccionRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionId;
import org.parangaricutirimicuaro.revtech.domain.inspeccion.model.InspeccionTecnica;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador de salida: implementa {@code InspeccionRepositoryPort} con JPA, traduciendo entre el agregado y sus entidades JPA.
 */
@Component
public class InspeccionPersistenceAdapter implements InspeccionRepositoryPort {

    private final SpringDataInspeccionRepository repository;

    InspeccionPersistenceAdapter(SpringDataInspeccionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public InspeccionTecnica guardar(InspeccionTecnica inspeccion) {
        var guardada = repository.save(InspeccionPersistenceMapper.toEntity(inspeccion));
        return InspeccionPersistenceMapper.toDomain(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InspeccionTecnica> buscarPorId(InspeccionId id) {
        return repository.findById(id.valor()).map(InspeccionPersistenceMapper::toDomain);
    }
}
