package com.nexus.backend.mappers;

import com.nexus.backend.dtos.ClienteCriarRequest;
import com.nexus.backend.dtos.ClienteCriarResponse;
import com.nexus.backend.dtos.ClienteDeletarResponse;
import com.nexus.backend.dtos.ClienteEditarRequest;
import com.nexus.backend.dtos.ClienteEditarResponse;
import com.nexus.backend.dtos.ClienteListarResponse;
import com.nexus.backend.entity.Cliente;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper (componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "id", ignore = true)
    Cliente toEntity(ClienteCriarRequest dto);
    ClienteCriarResponse toDTO(Cliente cliente);
    List<ClienteListarResponse> toDTO(List<Cliente> cliente);
    ClienteEditarResponse toEditarDTO(Cliente cliente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping (target = "id", ignore = true)
    void editarEntity(ClienteEditarRequest dto, @MappingTarget Cliente entity);

    @Mapping (target = "excluido", constant = "true")
    ClienteDeletarResponse toDeletarDTO(Cliente cliente);
}