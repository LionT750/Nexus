package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexus.backend.enums.ModalidadePagamento;
import com.nexus.backend.enums.StatusPedido;

public record PedidoDetailResponse(
    
    Long id,

    String descricao,

    BigDecimal valorTotal,

    LocalDate dataCriacao,

    LocalDate dataVencimento,

    StatusPedido statusPedido,

    ModalidadePagamento modalidadePagamento,

    ClienteCriarResponse cliente,

    ColaboradorCriarResponse colaborador

) {
    
}
