package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class EstoqueInsuficienteException extends ErrorResponseException {

    public EstoqueInsuficienteException(Long estoqueDisponivel, Long baixaRequisitada) {
        super(HttpStatus.BAD_REQUEST);
        setDetail("A operação não foi executada pois o estoque disponível: " + estoqueDisponivel + " não é suficiente. Quantidade requisitada para baixa: " + baixaRequisitada);
    }
}

