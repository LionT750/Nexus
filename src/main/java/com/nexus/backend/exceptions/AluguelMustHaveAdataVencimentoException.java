package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class AluguelMustHaveAdataVencimentoException extends ErrorResponseException {
    
    public AluguelMustHaveAdataVencimentoException() {
        super(HttpStatus.BAD_REQUEST);
        setDetail("Para alugueis, e necessario informar a data de vencimento do aluguel");
    }
}
