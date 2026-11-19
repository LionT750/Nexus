package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class QuantidadeMinimaParaAdicionarException extends ErrorResponseException {

    public QuantidadeMinimaParaAdicionarException(){
        super(HttpStatus.BAD_REQUEST);
        setDetail("É preciso informar uma quantidade maior que 0 para adição de estoque");
    }
    
}
