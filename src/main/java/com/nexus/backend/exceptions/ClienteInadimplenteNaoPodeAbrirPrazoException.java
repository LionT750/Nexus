package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;


public class ClienteInadimplenteNaoPodeAbrirPrazoException extends ErrorResponseException {
    
    public ClienteInadimplenteNaoPodeAbrirPrazoException(){
        super(HttpStatus.FORBIDDEN);
        setDetail("O cliente tem pendencias em aberto. Portanto, o pedido não pode ser gerado. Somente pedidos a vista ou pix são aceitos");
    }
}