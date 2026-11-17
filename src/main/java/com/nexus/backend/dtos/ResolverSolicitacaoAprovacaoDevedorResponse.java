package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexus.backend.enums.StatusAprovacaoFiadoParaDevedor;

public record ResolverSolicitacaoAprovacaoDevedorResponse (
    Long id,
    String cnpjCliente,
    String nomeColaborador,
    BigDecimal valorTotal,
    StatusAprovacaoFiadoParaDevedor statusAprovacaoFiadoParaDevedor,
    LocalDate dataAtualizacaoAprovacaoFiado
) {
    
}

