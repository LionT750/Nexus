package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ItemPedidoListarResponse(
    Long codigoIdProduto,
    BigDecimal quantidade
)
{}
