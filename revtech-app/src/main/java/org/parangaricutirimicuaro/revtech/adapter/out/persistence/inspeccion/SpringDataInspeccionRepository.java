package org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.inspeccion.entity.InspeccionTecnicaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataInspeccionRepository extends JpaRepository<InspeccionTecnicaJpaEntity, Long> {
}
