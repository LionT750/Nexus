package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class EmailJaCadastradoEmOutroClienteException extends ErrorResponseException {
    public EmailJaCadastradoEmOutroClienteException(String email) {
        super(HttpStatus.CONFLICT);
        setDetail("O email informado já está cadastrado em outro cliente " + email);
    }
    
}

