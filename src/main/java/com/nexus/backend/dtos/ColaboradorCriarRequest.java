package com.nexus.backend.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ColaboradorCriarRequest (
    @NotBlank String name,
    @CPF @NotNull String cpf,
    @Email @NotNull String email
) {
    
}
