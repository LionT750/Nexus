package com.nexus.backend.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexus.backend.dtos.ColaboradorDeletarResponse;
import com.nexus.backend.dtos.PedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarResponse;
import com.nexus.backend.dtos.PedidoDeletarResponse;
import com.nexus.backend.dtos.PedidoDetailResponse;
import com.nexus.backend.dtos.PedidoDevolverResponse;
import com.nexus.backend.dtos.PedidoFinalizarResponse;
import com.nexus.backend.dtos.PedidoListarResponse;
import com.nexus.backend.dtos.ResolverSolicitacaoDescontoResponse;
import com.nexus.backend.dtos.SolicitacaoDescontoListarResponse;
import com.nexus.backend.entity.Colaborador;
import com.nexus.backend.entity.ItemPedido;
import com.nexus.backend.entity.Pedido;

@Mapper (componentModel = "spring", uses = {ColaboradorMapper.class, ClienteMapper.class, ItemPedidoMapper.class})
public interface PedidoMapper{
    
    @Mapping (target = "id", ignore = true)
    @Mapping (target = "dataCriacao", ignore = true)
    @Mapping (target = "statusPedido", ignore = true)
    @Mapping (target = "cliente", ignore = true)
    @Mapping (target = "colaboradorDesconto", ignore = true)
    @Mapping (target = "itemsPedido", ignore = true)
    @Mapping( target = "valorTotal", ignore = true)
    @Mapping (target = "dataAtualizacaoDesconto", ignore = true)
    @Mapping (target = "descontoAutorizado", ignore = true)
    @Mapping (target = "statusAprovacaoDesconto", ignore = true)
    Pedido toEntity(PedidoCriarRequest dto);
    
    @Mapping (source = "id", target = "codigoPedido")
    @Mapping (source = "cliente.name", target = "nomeCliente")
    PedidoCriarResponse toDTO(Pedido  pedido);

    @Mapping (source = "itemsPedido", target = "itensPedido")
    PedidoDetailResponse toDetailDTO(Pedido pedido);
    
    @Mapping (source = "id", target = "codigoPedido")
    @Mapping (source = "cliente.name", target = "nomeCliente")
    PedidoListarResponse toListarDTO(Pedido pedido);

    @Mapping (source = "cliente.cnpj", target = "cnpjCliente")
    SolicitacaoDescontoListarResponse toDescontoDTO(Pedido pedido);

    List<SolicitacaoDescontoListarResponse> toListarDescontoDTO(List<Pedido> pedidos);

    @Mapping (source = "cliente.cnpj", target = "cnpjCliente")
    @Mapping (source = "colaboradorDesconto.name", target = "nomeColaborador")
    ResolverSolicitacaoDescontoResponse toResolverDescontoDTO(Pedido pedido);

    List<PedidoListarResponse> toListarDTO(List<Pedido> pedidos);

    @Mapping (source = "cliente.cnpj", target = "cnpjCliente")
    PedidoFinalizarResponse toFinalizarDTO(Pedido pedido);

    @Mapping (source = "pedido.id", target = "id")
    @Mapping (source = "pedido.cliente.cnpj", target = "cnpjCliente")
    @Mapping (source = "alugueisDevolvidos", target = "alugueis")
    PedidoDevolverResponse toDevolverDTO(Pedido pedido, List<ItemPedido> alugueisDevolvidos);

    @Mapping (target = "excluido", constant = "true")
    PedidoDeletarResponse toDeletarDTO(Pedido pedido);
}

