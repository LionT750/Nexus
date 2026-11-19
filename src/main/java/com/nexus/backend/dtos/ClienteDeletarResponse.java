package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.ClienteClassificao;

public record ClienteDeletarResponse(
    String name,
    String telefone,
    String cnpj,
    String email,
    BigDecimal limiteDeCredito,
    ClienteClassificao classificacao,
    boolean excluido
) {
    
}