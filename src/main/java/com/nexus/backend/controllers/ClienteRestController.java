package com.nexus.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.backend.dtos.ClienteCriarRequest;
import com.nexus.backend.dtos.ClienteCriarResponse;
import com.nexus.backend.dtos.ClienteListarResponse;
import com.nexus.backend.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/clientes")
@RequiredArgsConstructor 
public class ClienteRestController {
    private final ClienteService clienteService;

    @PostMapping
    public ClienteCriarResponse criarCliente(@Valid @RequestBody ClienteCriarRequest cliente) {
        return clienteService.criar(cliente);
    }

    @GetMapping
    public List<ClienteListarResponse> listarClientes() {
        return clienteService.listar();
    }
    
    


}
