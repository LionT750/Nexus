package com.nexus.backend.dtos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.nexus.backend.enums.ClienteClassificao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;

public record ClienteEditarRequest(
    @Length(min= 3, max=60) String name,
    String telefone,
    @CNPJ(format = CNPJ.Format.ALPHANUMERIC) String cnpj,
    @Email String email,
    @DecimalMin("0.0")BigDecimal limiteDeCredito,
    ClienteClassificao classificacao
) {
    
}
