package com.nexus.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.backend.dtos.ColaboradorCriarRequest;
import com.nexus.backend.dtos.ColaboradorCriarResponse;
import com.nexus.backend.dtos.ColaboradorDeletarResponse;
import com.nexus.backend.dtos.ColaboradorEditarRequest;
import com.nexus.backend.dtos.ColaboradorEditarResponse;
import com.nexus.backend.dtos.ColaboradorListarResponse;
import com.nexus.backend.dtos.ColaboradorReativarResponse;
import com.nexus.backend.service.ColaboradorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



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
    
    @PutMapping("/{id}/editar")
    public ColaboradorEditarResponse editar(@PathVariable Long id, @Valid @RequestBody ColaboradorEditarRequest colaboradorEditarRequest) {
        return colaboradorService.editar(id, colaboradorEditarRequest);
    }

    @DeleteMapping("/{id}")
    public ColaboradorDeletarResponse deletar(@PathVariable Long id) {
        return colaboradorService.deletar(id);
    }

    @PutMapping("/{id}/reativar")
    public ColaboradorReativarResponse reativar(@PathVariable Long id) {
        return colaboradorService.reativar(id);
    }


}
