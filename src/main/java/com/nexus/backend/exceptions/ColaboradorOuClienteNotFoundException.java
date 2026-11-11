package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ColaboradorOuClienteNotFoundException extends ErrorResponseException {

    public ColaboradorOuClienteNotFoundException() {
        super(HttpStatus.BAD_REQUEST);
        setDetail("A operação não foi executada pois o cliente ou colaborador informado na venda não existe");
    }
}

