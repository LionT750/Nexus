package com.nexus.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long>{
    boolean existsByName(String name);

    Optional<Colaborador> findByName(String name);
}
