package com.nexus.backend.mappers;

import com.nexus.backend.dtos.ClienteCriarRequest;
import com.nexus.backend.dtos.ClienteCriarResponse;
import com.nexus.backend.dtos.ClienteListarResponse;
import com.nexus.backend.entity.Cliente;

import java.util.List;

import org.mapstruct.Mapper;


@Mapper (componentModel = "spring")
public interface ClienteMapper {
    Cliente toEntity(ClienteCriarRequest dto);
    ClienteCriarResponse toDTO(Cliente cliente);
    List<ClienteListarResponse> toDTO(List<Cliente> cliente);
}