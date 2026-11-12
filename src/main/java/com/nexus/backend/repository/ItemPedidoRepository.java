package com.nexus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.ItemPedido;
import java.util.List;


public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
    List<ItemPedido> findByDataVencimentoAluguelIsNotNull();
}

