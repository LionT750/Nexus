package com.nexus.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Pedido;
import com.nexus.backend.enums.StatusPedido;


public interface PedidoRepository extends JpaRepository<Pedido, Long>{

    List<Pedido> findByDataVencimentoBeforeAndStatus(LocalDateTime now, StatusPedido status);
    
}

