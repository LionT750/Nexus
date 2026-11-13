package com.nexus.backend.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoCriarRequest(
    @NotNull Long codigoIdProduto,
    @NotNull @Min(1) Long quantidade,
    @NotNull boolean isAluguel,
    @Future LocalDate dataVencimentoAluguel
)
{}
