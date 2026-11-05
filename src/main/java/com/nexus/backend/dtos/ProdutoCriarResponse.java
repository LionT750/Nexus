package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record ProdutoCriarResponse(
    Long id,
    String nome,
    String descricao,
    Long estoque,
    BigDecimal preco) 
{}
