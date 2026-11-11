package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ClienteAlreadyCadastradoException extends ErrorResponseException {

    public ClienteAlreadyCadastradoException(String cnpjCliente) {
        super(HttpStatus.CONFLICT);
        setDetail("A operação não foi executada pois o cliente informado CNPJ: " + cnpjCliente + " ja esta cadastrado no sistema");
    }
}

