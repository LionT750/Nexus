package com.nexus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long>{
    
}
