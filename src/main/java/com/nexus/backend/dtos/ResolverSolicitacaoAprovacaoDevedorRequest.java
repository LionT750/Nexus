package com.nexus.backend.dtos;

import com.nexus.backend.enums.StatusAprovacaoFiadoParaDevedor;

import jakarta.validation.constraints.NotNull;

public record ResolverSolicitacaoAprovacaoDevedorRequest(
    @NotNull Long idColaborador,
    @NotNull StatusAprovacaoFiadoParaDevedor statusAprovacaoFiadoParaDevedor
) 
{
    
}

