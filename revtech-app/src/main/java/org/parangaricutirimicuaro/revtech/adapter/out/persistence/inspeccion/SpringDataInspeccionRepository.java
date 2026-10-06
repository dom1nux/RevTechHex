package org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity.InspeccionTecnicaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data de las inspecciones; solo lo usa el adaptador de persistencia.
 */
interface SpringDataInspeccionRepository extends JpaRepository<InspeccionTecnicaJpaEntity, Long> {
}
