package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ProdutoEditarResponse(
    String nome,
    String descricao,
    BigDecimal precoVenda,
    BigDecimal precoAluguel
) {
    
}
