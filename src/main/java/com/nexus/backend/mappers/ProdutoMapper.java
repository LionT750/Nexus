package com.nexus.backend.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nexus.backend.dtos.ProdutoAdicionarEstoqueResponse;
import com.nexus.backend.dtos.ProdutoCriarRequest;
import com.nexus.backend.dtos.ProdutoCriarResponse;
import com.nexus.backend.dtos.ProdutoDeletarResponse;
import com.nexus.backend.dtos.ProdutoEditarRequest;
import com.nexus.backend.dtos.ProdutoEditarResponse;
import com.nexus.backend.dtos.ProdutoListarResponse;
import com.nexus.backend.entity.Produto;

@Mapper (componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusProduto", ignore = true)
    Produto toEntity(ProdutoCriarRequest dto);
    
    ProdutoCriarResponse toDTO(Produto  produto);

    @Mapping(source = "produto.id", target = "idProduto")
    @Mapping(source = "produto.nome", target = "nomeProduto")
    ProdutoAdicionarEstoqueResponse toEstoqueDTO(Produto produto);

    ProdutoListarResponse toListarDTO(Produto produto);
    
    List<ProdutoListarResponse> toDTO(List<Produto > produtos);

    ProdutoEditarResponse toEditarDTO(Produto produto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estoque", ignore = true)
    @Mapping(target = "statusProduto", ignore = true)
    void editarEntity(ProdutoEditarRequest dto, @MappingTarget Produto entity);

    @Mapping(target = "excluido", constant = "true")
    ProdutoDeletarResponse toDeletarDTO(Produto produto);
}
