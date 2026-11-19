package com.nexus.backend.dtos;

public record ProdutoAdicionarEstoqueResponse(
    Long idProduto,
    String nomeProduto,
    Long estoque
) {
   

}
