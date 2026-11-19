package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class EmailJaCadastradoOutroColaboradorException extends ErrorResponseException {
   
    public EmailJaCadastradoOutroColaboradorException(String email) {
        super(HttpStatus.CONFLICT);
        setDetail("O email informado já está cadastrado em outro colaborador. Email:  " + email);
    }
    
}
