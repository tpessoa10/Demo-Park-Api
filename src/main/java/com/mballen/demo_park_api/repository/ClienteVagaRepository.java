package com.mballen.demo_park_api.repository;

import com.mballen.demo_park_api.entity.ClienteVaga;
import com.mballen.demo_park_api.repository.projection.ClienteVagaProjection;
import com.mballen.demo_park_api.web.controller.dto.PageableDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {
    Optional<ClienteVaga> findByReciboAndDataSaidaIsNull(String recibo);

    long countByClienteCpfAndDataSaidaIsNotNull(String cpf);

    Page<ClienteVagaProjection> findAllByClienteCpf(String cpf, PageableDto pageable);
}
