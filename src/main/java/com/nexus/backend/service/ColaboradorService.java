package com.nexus.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.backend.dtos.ColaboradorCriarRequest;
import com.nexus.backend.dtos.ColaboradorCriarResponse;
import com.nexus.backend.dtos.ColaboradorListarResponse;
import com.nexus.backend.entity.Colaborador;
import com.nexus.backend.mappers.ColaboradorMapper;
import com.nexus.backend.repository.ColaboradorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class ColaboradorService {
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorMapper colaboradorMapper;

    public ColaboradorCriarResponse criar(ColaboradorCriarRequest novoColaborador) {
        Colaborador colaborador = colaboradorMapper.toEntity(novoColaborador);
        Colaborador persistedColaborador = colaboradorRepository.save(colaborador);
        return colaboradorMapper.toDTO(persistedColaborador);
        
    }

    public List<ColaboradorListarResponse> listar(){
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        return colaboradorMapper.toDTO(colaboradores);
    }
  
}

