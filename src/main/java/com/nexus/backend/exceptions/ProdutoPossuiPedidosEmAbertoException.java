package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ProdutoPossuiPedidosEmAbertoException extends ErrorResponseException {

    public ProdutoPossuiPedidosEmAbertoException(Long id) {
        super(HttpStatus.CONFLICT);
        setDetail("O produto de id: " + id + " não pode ser excluído pois possui pedidos pendentes ou aluguéis não devolvidos");
    }
}
