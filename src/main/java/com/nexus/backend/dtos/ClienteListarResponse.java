package com.nexus.backend.dtos;

import com.nexus.backend.enums.ClienteCategoria;

public record ClienteListarResponse(
    String name,
    String telefone,
    String email,
    ClienteCategoria categoria
) 

{}
