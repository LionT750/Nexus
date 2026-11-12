package com.nexus.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.backend.dtos.ColaboradorCriarRequest;
import com.nexus.backend.dtos.ColaboradorCriarResponse;
import com.nexus.backend.dtos.ColaboradorListarResponse;
import com.nexus.backend.service.ColaboradorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/colaboradores")
@RequiredArgsConstructor 
public class ColaboradorRestController {
    private final ColaboradorService colaboradorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ColaboradorCriarResponse criarColaborador(@Valid @RequestBody ColaboradorCriarRequest colaborador) {
        return colaboradorService.criar(colaborador);
    }

    @GetMapping
    public List<ColaboradorListarResponse> listarColaboradores() {
        return colaboradorService.listar();
    }
    
    


}
