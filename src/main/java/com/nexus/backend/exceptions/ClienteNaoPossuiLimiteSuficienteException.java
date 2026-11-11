package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ClienteNaoPossuiLimiteSuficienteException extends ErrorResponseException {
    
    public ClienteNaoPossuiLimiteSuficienteException(String limite) {
        super(HttpStatus.FORBIDDEN);
        setDetail("O limite de crédito de : " + limite + " não cobre o valor total do pedido");
    }
}
