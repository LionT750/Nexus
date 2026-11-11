package com.nexus.backend.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoCriarRequest(
    @NotNull Long codigoIdProduto,
    @NotNull @Min(1) Long quantidade
)
{}
