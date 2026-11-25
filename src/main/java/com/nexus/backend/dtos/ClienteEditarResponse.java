package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.ClienteClassificao;

public record ClienteEditarResponse(
    String name,
    String telefone,
    String cnpj,
    String email,
    BigDecimal limiteDeCredito,
    ClienteClassificao classificacao
) {
    
}
