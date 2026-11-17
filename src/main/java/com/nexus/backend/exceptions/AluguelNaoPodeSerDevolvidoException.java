package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class AluguelNaoPodeSerDevolvidoException extends ErrorResponseException {
 
    public AluguelNaoPodeSerDevolvidoException(String mensagem) {
        super(HttpStatus.CONFLICT);
        setDetail(mensagem);
    }
}
