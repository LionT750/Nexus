package com.nexus.backend.mappers;

import com.nexus.backend.dtos.ColaboradorCriarRequest;
import com.nexus.backend.dtos.ColaboradorCriarResponse;
import com.nexus.backend.dtos.ColaboradorDeletarResponse;
import com.nexus.backend.dtos.ColaboradorEditarRequest;
import com.nexus.backend.dtos.ColaboradorEditarResponse;
import com.nexus.backend.dtos.ColaboradorListarResponse;
import com.nexus.backend.dtos.ColaboradorReativarResponse;
import com.nexus.backend.entity.Colaborador;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper (componentModel = "spring")
public interface ColaboradorMapper {

    @Mapping(target = "id", ignore = true)
    Colaborador toEntity(ColaboradorCriarRequest dto);
    
    ColaboradorCriarResponse toDTO(Colaborador colaborador);
    
    List<ColaboradorListarResponse> toDTO(List<Colaborador> colaboradores);

    ColaboradorEditarResponse toEditarDTO(Colaborador colaborador);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping (target = "id", ignore = true)
    void editarEntity(ColaboradorEditarRequest dto, @MappingTarget Colaborador entity);

    @Mapping (target = "excluido", constant = "true")
    ColaboradorDeletarResponse toDeletarDTO(Colaborador colaborador);

    @Mapping (target = "excluido", constant = "false")
    ColaboradorReativarResponse toReativarDTO(Colaborador colaborador);
}
