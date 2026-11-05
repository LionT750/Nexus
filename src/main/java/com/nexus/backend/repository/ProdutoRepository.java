package com.nexus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    
}

