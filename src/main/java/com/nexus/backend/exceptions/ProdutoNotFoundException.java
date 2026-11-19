package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ProdutoNotFoundException extends ErrorResponseException {

    public ProdutoNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND);
        setDetail("O produto de id : " + id + " não existe");
    }
    
}
