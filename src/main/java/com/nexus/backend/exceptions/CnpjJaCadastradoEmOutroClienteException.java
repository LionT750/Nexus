package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class CnpjJaCadastradoEmOutroClienteException extends ErrorResponseException {
    public CnpjJaCadastradoEmOutroClienteException(String cnpj) {
        super(HttpStatus.CONFLICT);
        setDetail("O cnpj informado já está cadastrado em outro cliente " + cnpj);
    }
    
}
