package com.nexus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    
}

