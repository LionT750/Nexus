package com.nexus.backend.entity.mock;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexus.backend.entity.Produto;
import com.nexus.backend.enums.StatusProduto;
import com.nexus.backend.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProdutoMock {

    private final ProdutoRepository produtoRepository;

    public void carregar() {
        if (produtoRepository.count() > 0)
            return;

        produtoRepository.saveAll(List.of(
            produto("Furadeira de Impacto 650W", "Furadeira profissional com maleta", 25, "389.90", "45.00", StatusProduto.DISPONIVEL),
            produto("Andaime Tubular 1,5m", "Modulo de andaime galvanizado", 60, "720.00", "35.00", StatusProduto.DISPONIVEL),
            produto("Betoneira 400L", "Betoneira motor eletrico 2CV", 8, "3250.00", "180.00", StatusProduto.DISPONIVEL),
            produto("Serra Circular 7 1/4", "Serra circular 1800W", 15, "529.90", "60.00", StatusProduto.DISPONIVEL),
            produto("Compactador de Solo", "Compactador a gasolina 5,5HP", 4, "5890.00", "320.00", StatusProduto.DISPONIVEL),
            produto("Gerador 5kVA", "Gerador diesel monofasico", 3, "8900.00", "450.00", StatusProduto.DISPONIVEL),
            produto("Lavadora de Alta Pressao", "Lavadora 2200 PSI", 12, "1190.00", "90.00", StatusProduto.DISPONIVEL),
            produto("Martelete Rompedor 10kg", "Martelete SDS Max", 0, "2780.00", "210.00", StatusProduto.INDISPONIVEL),
            produto("Escada Extensivel 7m", "Escada de aluminio", 1, "950.00", "55.00", StatusProduto.DISPONIVEL),
            produto("Talhadeira Manual (linha antiga)", "Descontinuada", 2, "49.90", "5.00", StatusProduto.DESATIVADO)
        ));
    }

    private Produto produto(String nome, String descricao, long estoque,
                            String precoVenda, String precoAluguel, StatusProduto status) {
        return Produto.builder()
            .nome(nome)
            .descricao(descricao)
            .estoque(estoque)
            .precoVenda(new BigDecimal(precoVenda))
            .precoAluguel(new BigDecimal(precoAluguel))
            .statusProduto(status)
            .build();
    }
}
