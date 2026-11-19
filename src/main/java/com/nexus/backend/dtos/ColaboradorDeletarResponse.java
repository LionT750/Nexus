package com.nexus.backend.dtos;

public record ColaboradorDeletarResponse(
    String name,
    String cpf,
    String email,
    boolean excluido   
) {
    
}
