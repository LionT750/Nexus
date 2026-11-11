package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record PedidoListarResponse 
    (
    Long codigoPedido,
    BigDecimal valorTotal,
    String nomeCliente
    ) 
{} 

