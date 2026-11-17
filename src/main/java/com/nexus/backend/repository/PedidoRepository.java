package com.nexus.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.backend.entity.Pedido;
import com.nexus.backend.enums.StatusAprovacaoDesconto;
import com.nexus.backend.enums.StatusAprovacaoFiadoParaDevedor;
import com.nexus.backend.enums.StatusPedido;


public interface PedidoRepository extends JpaRepository<Pedido, Long>{

    List<Pedido> findByDataVencimentoBeforeAndStatusPedido(LocalDate now, StatusPedido status);

    boolean existsByDataVencimentoBeforeAndStatusPedidoAndClienteId(LocalDate now, StatusPedido status, Long idCliente);

    List<Pedido> findByStatusPedidoAndStatusAprovacaoDesconto(StatusPedido statusPedido, StatusAprovacaoDesconto statusAprovacaoDesconto);

    List<Pedido> findByStatusPedidoAndStatusAprovacaoFiadoParaDevedor(StatusPedido statusPedido, StatusAprovacaoFiadoParaDevedor statusAprovacaoDevedor);

    List<Pedido> findByClienteCnpj(String cnpjCliente);

    List<Pedido> findByClienteId(Long id);

    List<Pedido> findByClienteEmail(String email);
    
}

