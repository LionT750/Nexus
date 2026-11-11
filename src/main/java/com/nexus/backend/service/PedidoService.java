package com.nexus.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.backend.dtos.PedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarResponse;
import com.nexus.backend.dtos.PedidoListarResponse;
import com.nexus.backend.entity.Cliente;
import com.nexus.backend.entity.Colaborador;
import com.nexus.backend.entity.ItemPedido;
import com.nexus.backend.entity.Pedido;
import com.nexus.backend.enums.ClienteClassificao;
import com.nexus.backend.enums.StatusPedido;
import com.nexus.backend.exceptions.ColaboradorOuClienteNotFoundException;
import com.nexus.backend.mappers.ColaboradorMapper;
import com.nexus.backend.mappers.ItemPedidoMapper;
import com.nexus.backend.mappers.PedidoMapper;
import com.nexus.backend.repository.ClienteRepository;
import com.nexus.backend.repository.ColaboradorRepository;
import com.nexus.backend.repository.PedidoRepository;
import com.nexus.backend.repository.ProdutoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper pedidoMapper;
    private final ItemPedidoMapper itemPedidoMapper;

    @Transactional 
    public PedidoCriarResponse criar(PedidoCriarRequest novoPedido) {
            if(!validarClienteColaboradorExistem(novoPedido))
                throw new ColaboradorOuClienteNotFoundException();

            Cliente cliente = clienteRepository.findByCnpj(novoPedido.CNPJcliente());
            Colaborador colaborador = colaboradorRepository.findByName(novoPedido.colaboradorNome());

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setColaborador(colaborador);
            pedido.setDataVencimento(novoPedido.dataVencimento());
            pedido.setDescricao(novoPedido.descricao());
            List<ItemPedido> itens = itemPedidoMapper.toEntityList(novoPedido.itensPedido());
            for (ItemPedido i : itens)
            {
                i.calcularValorItem();
            }
            pedido.setItemsPedido(itens);
            pedido.setModalidadePagamento(novoPedido.modalidadePagamento());
            pedido.setStatusPedido(StatusPedido.DISPONIVEL);
            
            BigDecimal desconto = validarQualMelhorDescontoProCliente(cliente, novoPedido);

            pedido.calcularEsetarValorTotal(desconto);

            return pedidoMapper.toDTO(pedidoRepository.save(pedido));
            

        
    }

    public List<PedidoListarResponse> listar(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidoMapper.toDTO(pedidos);
    }




    public boolean validarClienteColaboradorExistem(PedidoCriarRequest pedidoCriarRequest){
        if (colaboradorRepository.existsByName(pedidoCriarRequest.colaboradorNome()) && clienteRepository.existsByCnpj(pedidoCriarRequest.CNPJcliente()))
            return true;
        return false;
    }

    public BigDecimal validarQualMelhorDescontoProCliente(Cliente cliente, PedidoCriarRequest pedidoCriarRequest){
        BigDecimal melhorDesconto = BigDecimal.ZERO;

        if (cliente.getClassificacao() == ClienteClassificao.VIP)
            melhorDesconto = BigDecimal.valueOf(0.05);

        if (melhorDesconto.compareTo(pedidoCriarRequest.descontoPermitidoPorcentagem()) < 0) {
            melhorDesconto = pedidoCriarRequest.descontoPermitidoPorcentagem();
        }

        return melhorDesconto;
    }
  
}

