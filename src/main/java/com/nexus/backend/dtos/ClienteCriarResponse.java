package com.nexus.backend.dtos;

import com.nexus.backend.enums.ClienteCategoria;

public record ClienteCriarResponse(
    Long id,
    String name,
    String email,
    ClienteCategoria categoria) 
{
}
