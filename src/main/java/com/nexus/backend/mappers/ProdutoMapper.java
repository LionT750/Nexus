package com.nexus.backend.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexus.backend.dtos.ProdutoCriarRequest;
import com.nexus.backend.dtos.ProdutoCriarResponse;
import com.nexus.backend.dtos.ProdutoListarResponse;
import com.nexus.backend.entity.Produto;

@Mapper (componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusProduto", ignore = true)
    Produto toEntity(ProdutoCriarRequest dto);
    
    ProdutoCriarResponse toDTO(Produto  produto);

    ProdutoListarResponse toListarDTO(Produto produto);
    
    List<ProdutoListarResponse> toDTO(List<Produto > produtos);
}

