package com.nexus.backend.dtos;

public record ColaboradorListarResponse (
    Long id,
    String name,
    String cpf,
    String email
) {}
