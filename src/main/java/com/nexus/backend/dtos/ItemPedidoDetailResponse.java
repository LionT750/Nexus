package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ItemPedidoDetailResponse(
    Long id,
    Long codigoIdProduto,
    String nomeProduto,
    Long quantidade,
    BigDecimal valorItem,
    boolean isAluguel,
    LocalDate dataVencimentoAluguel,
    LocalDate dataDevolucao
) {
}
