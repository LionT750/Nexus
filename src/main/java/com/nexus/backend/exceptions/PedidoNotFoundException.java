package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PedidoNotFoundException extends ErrorResponseException {

    public PedidoNotFoundException(Long idInformado) {
        super(HttpStatus.NOT_FOUND);
        setDetail("A operação não foi executada pois o pedido de id: " + idInformado + " não existe");
    }
}

