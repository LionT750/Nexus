package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ItemPedidoListarAlugueisResponse(
    String nomeProduto,
    BigDecimal quantidade,
    LocalDate dataVencimentoAluguel
)
{}