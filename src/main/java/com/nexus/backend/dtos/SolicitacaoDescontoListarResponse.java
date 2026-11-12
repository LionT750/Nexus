package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record SolicitacaoDescontoListarResponse (
    Long id,
    String cnpjCliente,
    BigDecimal valorTotal,
    BigDecimal descontoSolicitado

) {
    
}
