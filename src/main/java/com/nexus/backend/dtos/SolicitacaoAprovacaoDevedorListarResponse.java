package com.nexus.backend.dtos;

import java.math.BigDecimal;

public record SolicitacaoAprovacaoDevedorListarResponse( 
    Long id,
    String cnpjCliente,
    BigDecimal valorTotal,
    PedidoDetailResponse pedido
) 
{}
