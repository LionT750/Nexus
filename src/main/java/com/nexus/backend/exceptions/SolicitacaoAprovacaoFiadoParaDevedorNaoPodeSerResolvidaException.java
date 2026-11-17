package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class SolicitacaoAprovacaoFiadoParaDevedorNaoPodeSerResolvidaException extends ErrorResponseException {
    
    public SolicitacaoAprovacaoFiadoParaDevedorNaoPodeSerResolvidaException(String mensagem) {
        super(HttpStatus.CONFLICT);
        setDetail(mensagem);
    }
}
