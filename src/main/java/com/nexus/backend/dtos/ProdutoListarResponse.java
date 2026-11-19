package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.StatusProduto;

public record ProdutoListarResponse(
    Long id,
    String nome,
    String descricao,
    Long estoque,
    BigDecimal precoVenda,
    BigDecimal precoAluguel,
    StatusProduto statusProduto
)   
{}
