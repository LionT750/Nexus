package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record PedidoCriarResponse(
    Long codigoPedido,
    BigDecimal valorTotal,
    String nomeCliente
    ) 
{} 

