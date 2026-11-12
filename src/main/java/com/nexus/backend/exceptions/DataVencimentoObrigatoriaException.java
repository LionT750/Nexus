package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class DataVencimentoObrigatoriaException extends ErrorResponseException {
    
    public DataVencimentoObrigatoriaException() {
        super(HttpStatus.BAD_REQUEST);
        setDetail("A operação não foi executada pois pedidos A_PRAZO precisam de uma data de vencimento");
    }
}
