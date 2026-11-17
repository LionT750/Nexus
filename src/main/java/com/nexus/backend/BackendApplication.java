package com.nexus.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nexus.backend.entity.mock.ClienteMock;
import com.nexus.backend.entity.mock.ColaboradorMock;
import com.nexus.backend.entity.mock.ProdutoMock;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner carregarMocks(ClienteMock clienteMock,
			ColaboradorMock colaboradorMock,
			ProdutoMock produtoMock) {
		return args -> {
			clienteMock.carregar();
			colaboradorMock.carregar();
			produtoMock.carregar();
		};
	}
}
