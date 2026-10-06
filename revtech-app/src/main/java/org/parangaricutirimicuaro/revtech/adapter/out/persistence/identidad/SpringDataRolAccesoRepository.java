package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad.entity.RolAccesoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataRolAccesoRepository extends JpaRepository<RolAccesoJpaEntity, Long> {
}
