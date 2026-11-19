package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.nexus.backend.enums.ModalidadePagamento;
import com.nexus.backend.enums.StatusAprovacaoDesconto;
import com.nexus.backend.enums.StatusPedido;

public record PedidoDetailResponse(
    
    Long id,

    String descricao,

    BigDecimal valorTotal,

    BigDecimal descontoSolicitado,

    BigDecimal descontoAutorizado,

    LocalDate dataCriacao,

    LocalDate dataVencimento,

    StatusPedido statusPedido,

    StatusAprovacaoDesconto statusAprovacaoDesconto,

    ModalidadePagamento modalidadePagamento,

    ClienteCriarResponse cliente,

    ColaboradorCriarResponse colaboradorDesconto,

    List<ItemPedidoDetailResponse> itensPedido

) {
    
}
