package com.nexus.backend.dtos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.nexus.backend.enums.ClienteClassificao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClienteCriarRequest(
    @NotBlank @Length(min= 3, max=60) String name,
    String telefone,
    @CNPJ(format = CNPJ.Format.ALPHANUMERIC) @NotNull String cnpj,
    @Email @NotNull @NotEmpty String email,
    @DecimalMin("0.0")BigDecimal limiteDeCredito,
    @NotNull ClienteClassificao classificacao
) 
{}
