package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PedidoNaoPodeSerDeletadoException extends ErrorResponseException {

    public PedidoNaoPodeSerDeletadoException(Long id, String statusAtual) {
        super(HttpStatus.CONFLICT);
        setDetail("O pedido de id: " + id + " não pode ser excluído pois não está pendente, status atual: " + statusAtual);
    }
}
