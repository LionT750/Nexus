package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ClienteNotFoundException extends ErrorResponseException {

    public ClienteNotFoundException(String cnpjCliente) {
        super(HttpStatus.NOT_FOUND);
        setDetail("A operação não foi executada pois o cliente informado CNPJ: " + cnpjCliente + " na venda não existe");
    }
}

