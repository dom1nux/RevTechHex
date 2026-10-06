package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad;

import org.parangaricutirimicuaro.revtech.application.identidad.port.out.UsuarioRepositoryPort;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.Usuario;
import org.parangaricutirimicuaro.revtech.domain.identidad.model.UsuarioId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador de salida: implementa {@code UsuarioRepositoryPort} con JPA.
 */
@Component
@Transactional(readOnly = true)
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository repository;

    UsuarioPersistenceAdapter(SpringDataUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return IdentidadPersistenceMapper.toDomain(repository.save(IdentidadPersistenceMapper.toEntity(usuario)));
    }

    @Override
    public Optional<Usuario> buscarPorId(UsuarioId id) {
        return repository.findById(id.valor()).map(IdentidadPersistenceMapper::toDomain);
    }

    @Override
    public boolean existePorUsername(String username) {
        return repository.existsByUsername(username);
    }
}
