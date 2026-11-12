package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class SolicitacaoDescontoNaoPodeSerResolvidaException extends ErrorResponseException {
 
    public SolicitacaoDescontoNaoPodeSerResolvidaException(String mensagem) {
        super(HttpStatus.CONFLICT);
        setDetail(mensagem);
    }
}
