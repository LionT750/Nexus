package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ColaboradorDesativadoException extends ErrorResponseException {

    public ColaboradorDesativadoException(Long id) {
        super(HttpStatus.CONFLICT);
        setDetail("O colaborador informado está desativado. Utilize o endpoint PUT /colaboradores/" + id + "/reativar para reativá-lo");
    }
}
