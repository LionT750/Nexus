package com.nexus.backend.dtos;

import com.nexus.backend.enums.ClienteCategoria;

public record ClienteCriarRequest(
    String name,
    String telefone,
    String cpf,
    String email,
    ClienteCategoria categoria
) 
{}
