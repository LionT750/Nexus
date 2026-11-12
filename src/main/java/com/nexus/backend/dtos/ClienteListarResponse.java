package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.ClienteClassificao;

public record ClienteListarResponse(
    String name,
    String telefone,
    String email,
    BigDecimal limiteDeCredito,
    ClienteClassificao classificacao
) 

{}
