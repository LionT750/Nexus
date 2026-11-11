package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ItemPedidoCriarResponse(
    Long codigoIdProduto,
    BigDecimal quantidade
)
{}