package com.nexus.backend.dtos;

public record ColaboradorCriarResponse (
    Long id,
    String name,
    String cpf,
    String email
) {
    
}
