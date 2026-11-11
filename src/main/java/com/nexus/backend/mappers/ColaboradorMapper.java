package com.nexus.backend.mappers;

import com.nexus.backend.dtos.ColaboradorCriarRequest;
import com.nexus.backend.dtos.ColaboradorCriarResponse;
import com.nexus.backend.dtos.ColaboradorListarResponse;
import com.nexus.backend.entity.Colaborador;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper (componentModel = "spring")
public interface ColaboradorMapper {

    @Mapping(target = "id", ignore = true)
    Colaborador toEntity(ColaboradorCriarRequest dto);
    
    ColaboradorCriarResponse toDTO(Colaborador colaborador);
    
    List<ColaboradorListarResponse> toDTO(List<Colaborador> colaboradores);
}
