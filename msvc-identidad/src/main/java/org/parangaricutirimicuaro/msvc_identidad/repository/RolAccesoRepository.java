package org.parangaricutirimicuaro.msvc_identidad.repository;

import org.parangaricutirimicuaro.msvc_identidad.model.entity.RolAcceso;
import org.parangaricutirimicuaro.msvc_identidad.model.type.NombreRol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolAccesoRepository extends CrudRepository<RolAcceso, Long> {

    Optional<RolAcceso> findByNombreRol(NombreRol nombreRol);

    boolean existsByNombreRol(NombreRol nombreRol);
}
