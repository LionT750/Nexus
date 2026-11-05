package com.nexus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    
}
