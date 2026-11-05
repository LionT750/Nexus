package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ProdutoListarResponse(
    String nome,
    String descricao,
    Long estoque,
    BigDecimal preco
)   
{}
