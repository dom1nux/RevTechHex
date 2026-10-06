package org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad;

import org.parangaricutirimicuaro.revtech.adapter.out.persistence.identidad.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataUsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {

    boolean existsByUsername(String username);
}
