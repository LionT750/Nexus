package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.StatusAprovacaoDesconto;
import com.nexus.backend.enums.StatusPedido;

public record PedidoListarResponse 
    (
    Long codigoPedido,
    BigDecimal valorTotal,
    String nomeCliente,
    StatusAprovacaoDesconto statusAprovacaoDesconto,
    StatusPedido statusPedido
    ) 
{} 

