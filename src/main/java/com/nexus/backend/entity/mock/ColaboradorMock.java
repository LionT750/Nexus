package com.nexus.backend.entity.mock;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nexus.backend.entity.Colaborador;
import com.nexus.backend.repository.ColaboradorRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ColaboradorMock {

    private final ColaboradorRepository colaboradorRepository;

    public void carregar() {
        if (colaboradorRepository.count() > 0)
            return;

        colaboradorRepository.saveAll(List.of(
            Colaborador.builder().name("Marcos Antunes").cpf("15881399803").email("marcos.antunes@nexus.com").build(),
            Colaborador.builder().name("Juliana Prado").cpf("69879730917").email("juliana.prado@nexus.com").build(),
            Colaborador.builder().name("Rafael Menezes").cpf("11417075350").email("rafael.menezes@nexus.com").build(),
            Colaborador.builder().name("Camila Duarte").cpf("64556815177").email("camila.duarte@nexus.com").build()
        ));
    }
}
