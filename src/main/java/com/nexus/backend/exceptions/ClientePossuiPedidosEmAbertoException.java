package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ClientePossuiPedidosEmAbertoException extends ErrorResponseException {

    public ClientePossuiPedidosEmAbertoException(Long id) {
        super(HttpStatus.CONFLICT);
        setDetail("O cliente de id: " + id + " não pode ser excluído pois possui pedidos pendentes em aberto");
    }
}
