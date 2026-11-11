package com.nexus.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.backend.dtos.ClienteCriarRequest;
import com.nexus.backend.dtos.ClienteCriarResponse;
import com.nexus.backend.dtos.ClienteListarResponse;
import com.nexus.backend.entity.Cliente;
import com.nexus.backend.exceptions.ClienteAlreadyCadastradoException;
import com.nexus.backend.mappers.ClienteMapper;
import com.nexus.backend.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteCriarResponse criar(ClienteCriarRequest novoCliente) {
        
        if (clienteRepository.existsByCnpj(novoCliente.cnpj()))
            throw new ClienteAlreadyCadastradoException(novoCliente.cnpj());

        Cliente cliente = clienteMapper.toEntity(novoCliente);
        Cliente persistedCliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(persistedCliente);
        
    }

    public List<ClienteListarResponse> listar(){
        List<Cliente> clientes = clienteRepository.findAll();
        return clienteMapper.toDTO(clientes);
    }
  
}
