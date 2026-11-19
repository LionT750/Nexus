package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ClienteDesativadoException extends ErrorResponseException {

    public ClienteDesativadoException(Long id) {
        super(HttpStatus.CONFLICT);
        setDetail("O cliente informado está desativado. Utilize o endpoint PUT /clientes/" + id + "/reativar para reativá-lo");
    }
}
