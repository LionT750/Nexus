package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.nexus.backend.enums.ModalidadePagamento;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
 
public record PedidoCriarRequest(
    String descricao,
    String CNPJcliente,
    String colaboradorNome,
    @DecimalMax("0.1")
    @DecimalMin("0.05")
    BigDecimal descontoPermitidoPorcentagem,
    List<ItemPedidoCriarRequest> itensPedido,
    ModalidadePagamento modalidadePagamento,
    LocalDate dataVencimento
) {
    
}
