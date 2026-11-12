package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class InformarPeloMenosUmIdenficadorDoClienteException extends ErrorResponseException {

    public InformarPeloMenosUmIdenficadorDoClienteException() {
        super(HttpStatus.BAD_REQUEST);
        setDetail("Informe pelo menos um campo válido para identificar o cliente. Campos válidos: id, cnpj ou email");
    }
}
