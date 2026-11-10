package com.nexus.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.backend.dtos.ProdutoCriarRequest;
import com.nexus.backend.dtos.ProdutoCriarResponse;
import com.nexus.backend.dtos.ProdutoListarResponse;
import com.nexus.backend.service.ProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/produtos")
@RequiredArgsConstructor 
public class ProdutoRestController {
    private final ProdutoService produtoService;

    @PostMapping
    public ProdutoCriarResponse criarProduto(@Valid @RequestBody ProdutoCriarRequest produto) {
        return produtoService.criar(produto);
    }

    @GetMapping
    public List<ProdutoListarResponse> listarClientes() {
        return produtoService.listar();
    }
}
