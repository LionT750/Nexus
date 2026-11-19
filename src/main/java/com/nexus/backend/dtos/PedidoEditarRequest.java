package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.nexus.backend.enums.ModalidadePagamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;

public record PedidoEditarRequest(
    String descricao,
    
    @DecimalMax("0.15")
    @DecimalMin("0.0")
    BigDecimal descontoSolicitado,

    ModalidadePagamento modalidadePagamento,

    @Future LocalDate dataVencimento,
    
    List<@Valid ItemPedidoCriarRequest> itensPedido
) {
}
