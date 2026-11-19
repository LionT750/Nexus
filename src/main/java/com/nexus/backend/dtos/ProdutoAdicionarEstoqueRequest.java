package com.nexus.backend.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProdutoAdicionarEstoqueRequest(
    @NotNull
    Long idProduto,
    @NotNull 
    @Min(1)
    Long qtdAdicionar
) {
    
}
