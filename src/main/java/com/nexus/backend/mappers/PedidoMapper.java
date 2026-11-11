package com.nexus.backend.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexus.backend.dtos.PedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarResponse;
import com.nexus.backend.dtos.PedidoListarResponse;
import com.nexus.backend.entity.Pedido;

@Mapper (componentModel = "spring")
public interface PedidoMapper{
    
    Pedido toEntity(PedidoCriarRequest dto);
    
    PedidoCriarResponse toDTO(Pedido  pedido);
    
    List<PedidoListarResponse> toDTO(List<Pedido > pedidos);
}

