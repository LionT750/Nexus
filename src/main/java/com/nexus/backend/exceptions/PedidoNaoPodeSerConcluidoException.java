package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PedidoNaoPodeSerConcluidoException extends ErrorResponseException {
 
    public PedidoNaoPodeSerConcluidoException(String mensagem) {
        super(HttpStatus.CONFLICT);
        setDetail(mensagem);
    }
}
