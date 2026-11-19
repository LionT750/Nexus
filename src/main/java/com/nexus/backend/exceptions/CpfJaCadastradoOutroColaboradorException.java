package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class CpfJaCadastradoOutroColaboradorException extends ErrorResponseException {
   
    public CpfJaCadastradoOutroColaboradorException(String cpf) {
        super(HttpStatus.CONFLICT);
        setDetail("O cpf informado já está cadastrado em outro colaborador. CPF:  " + cpf);
    }
    
}
