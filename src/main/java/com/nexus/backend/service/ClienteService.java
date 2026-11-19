package com.nexus.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nexus.backend.dtos.ClienteCriarRequest;
import com.nexus.backend.dtos.ClienteCriarResponse;
import com.nexus.backend.dtos.ClienteDeletarResponse;
import com.nexus.backend.dtos.ClienteEditarRequest;
import com.nexus.backend.dtos.ClienteEditarResponse;
import com.nexus.backend.dtos.ClienteListarResponse;
import com.nexus.backend.dtos.ClienteReativarResponse;
import com.nexus.backend.entity.Cliente;
import com.nexus.backend.enums.StatusPedido;
import com.nexus.backend.exceptions.ClienteAlreadyCadastradoException;
import com.nexus.backend.exceptions.ClienteDesativadoException;
import com.nexus.backend.exceptions.ClienteNotFoundException;
import com.nexus.backend.exceptions.ClientePossuiPedidosEmAbertoException;
import com.nexus.backend.exceptions.CnpjJaCadastradoEmOutroClienteException;
import com.nexus.backend.exceptions.EmailJaCadastradoEmOutroClienteException;
import com.nexus.backend.exceptions.EmailJaCadastradoException;
import com.nexus.backend.mappers.ClienteMapper;
import com.nexus.backend.repository.ClienteRepository;
import com.nexus.backend.repository.PedidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final ClienteMapper clienteMapper;

    public ClienteCriarResponse criar(ClienteCriarRequest novoCliente) {
        
        Optional<Cliente> clienteDesativadoPorCnpj = clienteRepository.findDesativadoByCnpj(novoCliente.cnpj());
        if (clienteDesativadoPorCnpj.isPresent())
            throw new ClienteDesativadoException(clienteDesativadoPorCnpj.get().getId());

        Optional<Cliente> clienteDesativadoPorEmail = clienteRepository.findDesativadoByEmail(novoCliente.email());
        if (clienteDesativadoPorEmail.isPresent())
            throw new ClienteDesativadoException(clienteDesativadoPorEmail.get().getId());

        if (clienteRepository.existsByEmail(novoCliente.email()))
            throw new EmailJaCadastradoException(novoCliente.email());

        if (clienteRepository.existsByCnpj(novoCliente.cnpj()))
            throw new ClienteAlreadyCadastradoException(novoCliente.cnpj());

        Cliente cliente = clienteMapper.toEntity(novoCliente);

        if (cliente.getLimiteDeCredito() == null)
            cliente.setLimiteDeCredito(BigDecimal.ZERO);

        Cliente persistedCliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(persistedCliente);
        
    }

    public List<ClienteListarResponse> listar(){
        List<Cliente> clientes = clienteRepository.findAll();
        return clienteMapper.toDTO(clientes);
    }
  
    public ClienteEditarResponse editar(Long idCliente, ClienteEditarRequest dto) {
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new ClienteNotFoundException(idCliente));

        if (dto.cnpj() != null && !Objects.equals(cliente.getCnpj(), dto.cnpj()) && clienteRepository.existsByCnpj(dto.cnpj()))
            throw new CnpjJaCadastradoEmOutroClienteException(dto.cnpj());

        if (dto.email() != null && !Objects.equals(cliente.getEmail(), dto.email()) && clienteRepository.existsByEmail(dto.email()))
            throw new EmailJaCadastradoEmOutroClienteException(dto.email());

        clienteMapper.editarEntity(dto, cliente);
        clienteRepository.save(cliente);
        
        return clienteMapper.toEditarDTO(cliente);
    }

    @Transactional 
    public ClienteDeletarResponse deletar(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new ClienteNotFoundException(idCliente));

        if (pedidoRepository.existsByClienteIdAndStatusPedido(idCliente, StatusPedido.PENDENTE))
            throw new ClientePossuiPedidosEmAbertoException(idCliente);

        clienteRepository.delete(cliente);
        
        return clienteMapper.toDeletarDTO(cliente);
    }

    @Transactional
    public ClienteReativarResponse reativar(Long idCliente) {
        Cliente cliente = clienteRepository.findByIdIncludingDeleted(idCliente).orElseThrow(() -> new ClienteNotFoundException(idCliente));

        clienteRepository.reativar(idCliente);

        return clienteMapper.toReativarDTO(cliente);
    }
}
