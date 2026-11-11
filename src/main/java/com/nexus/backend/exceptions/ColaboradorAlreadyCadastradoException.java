package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ColaboradorAlreadyCadastradoException extends ErrorResponseException {

    public ColaboradorAlreadyCadastradoException(String nomeColaborador) {
        super(HttpStatus.CONFLICT);
        setDetail("A operação não foi executada pois o colaborador: " + nomeColaborador + " ja esta cadastrado no sistema");
    }
}

