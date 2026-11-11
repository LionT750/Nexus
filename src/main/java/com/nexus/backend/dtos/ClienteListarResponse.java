package com.nexus.backend.dtos;

import com.nexus.backend.enums.ClienteClassificao;

public record ClienteListarResponse(
    String name,
    String telefone,
    String email,
    ClienteClassificao classificacao
) 

{}
