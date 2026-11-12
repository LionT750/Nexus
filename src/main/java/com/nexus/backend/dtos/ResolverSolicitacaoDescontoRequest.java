package com.nexus.backend.dtos;

import com.nexus.backend.enums.StatusAprovacaoDesconto;

import jakarta.validation.constraints.NotNull;

public record ResolverSolicitacaoDescontoRequest(
    @NotNull Long idColaborador,
    @NotNull StatusAprovacaoDesconto statusAprovacaoDesconto
) {
    
}
