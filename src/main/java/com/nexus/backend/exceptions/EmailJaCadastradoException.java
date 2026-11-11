package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class EmailJaCadastradoException extends ErrorResponseException {

    public EmailJaCadastradoException(String email) {
        super(HttpStatus.CONFLICT);
        setDetail("A operação não foi executada pois o email informado: " + email + " ja esta cadastrado no sistema");
    }
}


