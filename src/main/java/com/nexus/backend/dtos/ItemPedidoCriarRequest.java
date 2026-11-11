package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ItemPedidoCriarRequest(
    Long codigoIdProduto,
    BigDecimal quantidade
)
{}
