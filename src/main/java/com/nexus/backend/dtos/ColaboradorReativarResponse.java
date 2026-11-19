package com.nexus.backend.dtos;

public record ColaboradorReativarResponse(
    String name,
    String cpf,
    String email,
    boolean excluido
) {
    
}
