package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ProdutoDeletarResponse(
    String nome,
    String descricao,
    Long estoque,
    BigDecimal precoVenda,
    BigDecimal precoAluguel,
    boolean excluido
) {
    
}
