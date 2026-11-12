package com.nexus.backend.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProdutoCriarRequest(
    @NotNull @NotEmpty String nome,
    String descricao,
    @NotNull Long estoque,
    @NotNull BigDecimal precoVenda,
    @NotNull BigDecimal precoAluguel
) 
{} 
