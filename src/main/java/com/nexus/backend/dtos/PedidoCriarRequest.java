package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.nexus.backend.enums.ModalidadePagamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
 
public record PedidoCriarRequest(
    String descricao,
    @NotNull String CNPJcliente,
    @NotNull String colaboradorNome,
    @DecimalMax("0.1")
    @DecimalMin("0.0")
    @NotNull 
    BigDecimal descontoPermitidoPorcentagem,
    @NotNull
    @NotEmpty 
    List<@Valid ItemPedidoCriarRequest> itensPedido,
    @NotNull ModalidadePagamento modalidadePagamento,
    @Future LocalDate dataVencimento
) {
    
}
