package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PedidoNaoPodeSerFinalizadoException extends ErrorResponseException {
 
    public PedidoNaoPodeSerFinalizadoException(String mensagem) {
        super(HttpStatus.CONFLICT);
        setDetail(mensagem);
    }
}
