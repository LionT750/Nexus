package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexus.backend.enums.StatusAprovacaoDesconto;

public record ResolverSolicitacaoDescontoResponse(
    Long id,
    String cnpjCliente,
    String nomeColaborador,
    BigDecimal valorTotal,
    BigDecimal descontoSolicitado,
    BigDecimal descontoAutorizado,
    StatusAprovacaoDesconto statusAprovacaoDesconto,
    LocalDate dataAtualizacaoDesconto
) {
    
}
