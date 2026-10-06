package org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence;

import org.parangaricutirimicuaro.msvc_inspection.application.port.out.InspeccionRepositoryPort;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionId;
import org.parangaricutirimicuaro.msvc_inspection.domain.model.InspeccionTecnica;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
