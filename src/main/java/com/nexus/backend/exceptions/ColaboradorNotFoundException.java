package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ColaboradorNotFoundException extends ErrorResponseException {

    public ColaboradorNotFoundException(String nomeColaborador) {
        super(HttpStatus.NOT_FOUND);
        setDetail("A operação não foi executada pois colaborador informado: " + nomeColaborador + " na venda não existe");
    }
}

