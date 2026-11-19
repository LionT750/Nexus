package com.nexus.backend.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;

public record ProdutoEditarRequest(
    @NotEmpty String nome,
    String descricao,
    BigDecimal precoVenda,
    BigDecimal precoAluguel
) {
    
}
