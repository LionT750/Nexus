package com.nexus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Cliente;
import java.util.List;


public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    boolean existsByCnpj(String cnpj);

    Cliente findByCnpj(String cnpj);
}
