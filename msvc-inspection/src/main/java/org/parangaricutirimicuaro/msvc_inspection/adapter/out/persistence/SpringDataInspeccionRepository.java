package org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence;

import org.parangaricutirimicuaro.msvc_inspection.adapter.out.persistence.entity.InspeccionTecnicaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataInspeccionRepository extends JpaRepository<InspeccionTecnicaJpaEntity, Long> {
}
