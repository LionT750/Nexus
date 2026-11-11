package com.nexus.backend.dtos;

import com.nexus.backend.enums.ClienteClassificao;

public record ClienteCriarResponse(
    Long id,
    String name,
    String email,
    ClienteClassificao classificacao) 
{
}
