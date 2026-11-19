package com.nexus.backend.dtos;

import jakarta.validation.constraints.Min;

public record ProdutoAdicionarEstoqueRequest(
    Long idProduto,
    @Min(1)
    Long qtdAdicionar
) {
    
}
