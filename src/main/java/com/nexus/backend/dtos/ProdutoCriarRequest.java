package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ProdutoCriarRequest(
    String nome,
    String descricao,
    Long estoque,
    BigDecimal preco
) 
{} 
