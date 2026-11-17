package com.nexus.backend.dtos;

import java.util.List;

public record PedidoDevolverResponse(
    Long id,
    String cnpjCliente,
    List<ItemPedidoListarAlugueisResponse> alugueis
) {
    
}
