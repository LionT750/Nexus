package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.ClienteClassificao;

public record ClienteListarResponse(
    Long id,
    String name,
    String cnpj,
    String telefone,
    String email,
    BigDecimal limiteDeCredito,
    ClienteClassificao classificacao
) 

{}
