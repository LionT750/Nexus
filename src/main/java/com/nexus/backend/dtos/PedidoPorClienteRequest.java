package com.nexus.backend.dtos;

import org.hibernate.validator.constraints.br.CNPJ;

import com.nexus.backend.exceptions.InformarPeloMenosUmIdenficadorDoClienteException;

import jakarta.validation.constraints.Email;

public record PedidoPorClienteRequest(
    Long idCliente,
    @CNPJ(format = CNPJ.Format.ALPHANUMERIC) String CNPJcliente,
    @Email String emailCliente
)
{
    public PedidoPorClienteRequest {

       if (idCliente == null && CNPJcliente == null && emailCliente == null)
            throw new InformarPeloMenosUmIdenficadorDoClienteException();
 }
}
