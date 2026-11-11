package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class NoValidProdutosForThisPedidoException extends ErrorResponseException {
    
    public NoValidProdutosForThisPedidoException() {
        super(HttpStatus.BAD_REQUEST);
        setDetail("Nao foram encontrados produtos para os codigos informados no pedido");
    }
}
