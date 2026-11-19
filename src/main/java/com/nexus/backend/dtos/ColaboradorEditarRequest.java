package com.nexus.backend.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;

public record ColaboradorEditarRequest(
    String name,
    @CPF String cpf,
    @Email String email
) {
    
}
