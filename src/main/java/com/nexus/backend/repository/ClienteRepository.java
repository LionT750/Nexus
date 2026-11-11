package com.nexus.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    boolean existsByCnpj(String cnpj);

    Optional<Cliente> findByCnpj(String cnpj);

    boolean existsByEmail(String email);
}
