package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record PedidoItemListarResponse(
    String nome,
    String descricao,
    BigDecimal preco
    ) 
{} 
