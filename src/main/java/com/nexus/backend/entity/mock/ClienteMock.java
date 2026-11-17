package com.nexus.backend.entity.mock;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nexus.backend.entity.Cliente;
import com.nexus.backend.enums.ClienteClassificao;
import com.nexus.backend.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClienteMock {

    private final ClienteRepository clienteRepository;

    public void carregar() {
        if (clienteRepository.count() > 0)
            return;

        clienteRepository.saveAll(List.of(
            Cliente.builder().name("Construtora Horizonte Ltda").cnpj("88447294000145")
                .telefone("(48) 3622-1001").email("compras@horizonte.com.br")
                .limiteDeCredito(new BigDecimal("50000.00")).classificacao(ClienteClassificao.VIP).build(),

            Cliente.builder().name("Engenharia Sul Obras SA").cnpj("05576169000108")
                .telefone("(48) 3622-2002").email("financeiro@sulobras.com.br")
                .limiteDeCredito(new BigDecimal("20000.00")).classificacao(ClienteClassificao.VIP).build(),

            Cliente.builder().name("Reformas Silva ME").cnpj("87168976000156")
                .telefone("(48) 99911-3003").email("contato@reformassilva.com.br")
                .limiteDeCredito(new BigDecimal("8000.00")).classificacao(ClienteClassificao.NORMAL).build(),

            Cliente.builder().name("Mercado Bom Preco Ltda").cnpj("84697880000197")
                .telefone("(48) 99922-4004").email("adm@bompreco.com.br")
                .limiteDeCredito(new BigDecimal("1500.00")).classificacao(ClienteClassificao.NORMAL).build(),

            Cliente.builder().name("Oficina do Ze Mecanica").cnpj("93208196000195")
                .telefone("(48) 99933-5005").email("ze@oficinadoze.com.br")
                .limiteDeCredito(BigDecimal.ZERO).classificacao(ClienteClassificao.NORMAL).build(),

            Cliente.builder().name("Locadora Tubarao Eventos").cnpj("72731778000181")
                .telefone("(48) 3622-6006").email("eventos@locadoratubarao.com.br")
                .limiteDeCredito(BigDecimal.ZERO).classificacao(ClienteClassificao.VIP).build()
        ));
    }
}
