package com.nexus.backend.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexus.backend.dtos.ItemPedidoCriarRequest;
import com.nexus.backend.dtos.ItemPedidoCriarResponse;
import com.nexus.backend.dtos.ItemPedidoListarResponse;
import com.nexus.backend.entity.ItemPedido;

    @Mapper (componentModel = "spring")
    public interface ItemPedidoMapper{
        
    ItemPedido toEntity(ItemPedidoCriarRequest dto);

    @Mapping(source = "codigoIdProduto", target = "produto.id")
    List<ItemPedido> toEntityList(List<ItemPedidoCriarRequest> itensPedidoDto);

    ItemPedidoCriarResponse toDTO(ItemPedido  itemPedido);
        
    List<ItemPedidoListarResponse> toDTO(List<ItemPedido > Itenspedido);
    }

