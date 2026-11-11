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
     
    @Mapping (target = "id", ignore = true)
    @Mapping (target = "produto", ignore = true)
    @Mapping (target = "valorItem", ignore = true)
    @Mapping( target = "pedido", ignore = true)
    ItemPedido toEntity(ItemPedidoCriarRequest dto);

    List<ItemPedido> toEntityList(List<ItemPedidoCriarRequest> itensPedidoDto);

    @Mapping (source = "id", target = "codigoIdProduto")
    ItemPedidoCriarResponse toDTO(ItemPedido  itemPedido);
     
    @Mapping (source = "id", target = "codigoIdProduto")
    ItemPedidoListarResponse toListarDTO(ItemPedido itemPedido);

    List<ItemPedidoListarResponse> toDTO(List<ItemPedido > Itenspedido);
    }

