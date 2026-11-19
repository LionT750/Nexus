package com.nexus.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus.backend.dtos.ColaboradorCriarRequest;
import com.nexus.backend.dtos.ColaboradorCriarResponse;
import com.nexus.backend.dtos.ColaboradorDeletarResponse;
import com.nexus.backend.dtos.ColaboradorEditarRequest;
import com.nexus.backend.dtos.ColaboradorEditarResponse;
import com.nexus.backend.dtos.ColaboradorListarResponse;
import com.nexus.backend.entity.Colaborador;
import com.nexus.backend.exceptions.ColaboradorAlreadyCadastradoException;
import com.nexus.backend.exceptions.ColaboradorNotFoundException;
import com.nexus.backend.exceptions.CpfJaCadastradoOutroColaboradorException;
import com.nexus.backend.exceptions.EmailJaCadastradoOutroColaboradorException;
import com.nexus.backend.mappers.ColaboradorMapper;
import com.nexus.backend.repository.ColaboradorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class ColaboradorService {
    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorMapper colaboradorMapper;

    public ColaboradorCriarResponse criar(ColaboradorCriarRequest novoColaborador) {

        if (colaboradorRepository.existsByName(novoColaborador.name()))
            throw new ColaboradorAlreadyCadastradoException(novoColaborador.name());


        Colaborador colaborador = colaboradorMapper.toEntity(novoColaborador);
        Colaborador persistedColaborador = colaboradorRepository.save(colaborador);
        return colaboradorMapper.toDTO(persistedColaborador);
        
    }

    public List<ColaboradorListarResponse> listar(){
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        return colaboradorMapper.toDTO(colaboradores);
    }

    public ColaboradorEditarResponse editar(Long idColaborador, ColaboradorEditarRequest dto) {
        Colaborador colaborador = colaboradorRepository.findById(idColaborador).orElseThrow(() -> new ColaboradorNotFoundException(idColaborador));

        if (dto.cpf() != null && colaborador.getCpf() != dto.cpf() && colaboradorRepository.existsByCpf(dto.cpf()))
            throw new CpfJaCadastradoOutroColaboradorException(dto.cpf());

        if (dto.email() != null && colaborador.getEmail() != dto.email() && colaboradorRepository.existsByEmail(dto.email()))
            throw new EmailJaCadastradoOutroColaboradorException(dto.email());

        colaboradorMapper.editarEntity(dto, colaborador);
        colaboradorRepository.save(colaborador);
        
        return colaboradorMapper.toEditarDTO(colaborador);
    }

    @Transactional 
    public ColaboradorDeletarResponse deletar(Long idColaborador) {
        Colaborador colaborador = colaboradorRepository.findById(idColaborador).orElseThrow(() -> new ColaboradorNotFoundException(idColaborador));

        colaboradorRepository.delete(colaborador);
        
        return colaboradorMapper.toDeletarDTO(colaborador);
    }
  
}

