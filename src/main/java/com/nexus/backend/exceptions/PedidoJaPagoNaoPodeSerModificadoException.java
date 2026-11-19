package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PedidoJaPagoNaoPodeSerModificadoException extends ErrorResponseException {

    public PedidoJaPagoNaoPodeSerModificadoException() {
        super(HttpStatus.FORBIDDEN);
        setDetail("Esse pedido já foi pago e não pode mais ser editado");
    }
    
}
