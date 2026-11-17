package com.nexus.backend.dtos;

import java.math.BigDecimal;

import com.nexus.backend.enums.ModalidadePagamento;
import com.nexus.backend.enums.StatusPedido;

public record PedidoFinalizarResponse(
    Long id,
    String cnpjCliente,
    BigDecimal valorTotal,
    ModalidadePagamento modalidadePagamento,
    StatusPedido statusPedido
) {
    
}
