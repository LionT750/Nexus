package com.nexus.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.br.CNPJ;

import com.nexus.backend.enums.ModalidadePagamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
 
public record PedidoCriarRequest(
    String descricao,
    @CNPJ(format = CNPJ.Format.ALPHANUMERIC) @NotNull String CNPJcliente,
    @DecimalMax("0.15")
    @DecimalMin("0.0")
    @NotNull 
    BigDecimal descontoSolicitado,
    @NotNull
    @NotEmpty 
    List<@Valid ItemPedidoCriarRequest> itensPedido,
    @NotNull ModalidadePagamento modalidadePagamento,
    @Future LocalDate dataVencimento
) {
    
}
