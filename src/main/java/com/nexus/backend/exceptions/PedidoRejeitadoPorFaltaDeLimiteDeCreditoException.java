package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PedidoRejeitadoPorFaltaDeLimiteDeCreditoException extends ErrorResponseException {

    public PedidoRejeitadoPorFaltaDeLimiteDeCreditoException() {
        super(HttpStatus.CONFLICT);
        setDetail("O pedido foi rejeitado pois o limite de crédito não é suficiente para cobrir o valor total");
    }
    
}
