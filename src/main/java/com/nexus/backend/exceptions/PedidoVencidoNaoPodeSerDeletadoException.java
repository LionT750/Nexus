package com.nexus.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PedidoVencidoNaoPodeSerDeletadoException extends ErrorResponseException {

    public PedidoVencidoNaoPodeSerDeletadoException() {
        super(HttpStatus.FORBIDDEN);
        setDetail("O pedido não pode ser deletado pois está vencido. Regularize o pagamento para poder deletar");
    }
    
}
