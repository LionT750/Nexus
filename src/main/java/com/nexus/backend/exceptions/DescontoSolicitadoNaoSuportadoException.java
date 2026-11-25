package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class DescontoSolicitadoNaoSuportadoException extends ErrorResponseException {
    public DescontoSolicitadoNaoSuportadoException(String descontoSolicitado) {
        super(HttpStatus.BAD_REQUEST);
        setDetail("O valor requisitado para desconto foi inválido. Solicitado : " + descontoSolicitado + " \nValores válidos: 0.0, 0.05, 0.10, 0.15");
    }
}
