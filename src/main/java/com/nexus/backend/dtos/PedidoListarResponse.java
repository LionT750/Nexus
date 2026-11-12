package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.StatusAprovacaoDesconto;

public record PedidoListarResponse 
    (
    Long codigoPedido,
    BigDecimal valorTotal,
    String nomeCliente,
    StatusAprovacaoDesconto statusAprovacaoDesconto
    ) 
{} 

