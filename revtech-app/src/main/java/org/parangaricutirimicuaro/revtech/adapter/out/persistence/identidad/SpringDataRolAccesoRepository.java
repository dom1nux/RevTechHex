package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad.entity.RolAccesoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data de roles; solo lo usa el adaptador de persistencia.
 */
interface SpringDataRolAccesoRepository extends JpaRepository<RolAccesoJpaEntity, Long> {
}
