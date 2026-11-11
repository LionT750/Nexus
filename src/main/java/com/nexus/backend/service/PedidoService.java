package com.nexus.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus.backend.dtos.ItemPedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarResponse;
import com.nexus.backend.dtos.PedidoDetailResponse;
import com.nexus.backend.dtos.PedidoListarResponse;
import com.nexus.backend.entity.Cliente;
import com.nexus.backend.entity.Colaborador;
import com.nexus.backend.entity.ItemPedido;
import com.nexus.backend.entity.Pedido;
import com.nexus.backend.entity.Produto;
import com.nexus.backend.enums.ClienteClassificao;
import com.nexus.backend.enums.ModalidadePagamento;
import com.nexus.backend.enums.StatusPedido;
import com.nexus.backend.exceptions.ClienteNaoPossuiLimiteSuficienteException;
import com.nexus.backend.exceptions.ClienteNotFoundException;
import com.nexus.backend.exceptions.ColaboradorNotFoundException;
import com.nexus.backend.exceptions.NoValidProdutosForThisPedidoException;
import com.nexus.backend.exceptions.PedidoNotFoundException;
import com.nexus.backend.mappers.PedidoMapper;
import com.nexus.backend.repository.ClienteRepository;
import com.nexus.backend.repository.ColaboradorRepository;
import com.nexus.backend.repository.PedidoRepository;
import com.nexus.backend.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper pedidoMapper;

    @Transactional 
    public PedidoCriarResponse criar(PedidoCriarRequest novoPedido) {
            
            Cliente cliente = clienteRepository.findByCnpj(novoPedido.CNPJcliente()).orElseThrow(() -> new ClienteNotFoundException(novoPedido.CNPJcliente()));
            Colaborador colaborador = colaboradorRepository.findByName(novoPedido.colaboradorNome()).orElseThrow(() -> new ColaboradorNotFoundException(novoPedido.colaboradorNome()));

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setColaborador(colaborador);
            pedido.setDataVencimento(novoPedido.dataVencimento());
            pedido.setDescricao(novoPedido.descricao());
            
            List<ItemPedido> itensPedidos = criarItemPedidos(novoPedido, pedido);

            pedido.setItemsPedido(itensPedidos);
            pedido.setModalidadePagamento(novoPedido.modalidadePagamento());
            pedido.setStatusPedido(StatusPedido.PENDENTE);
            
            BigDecimal desconto = validarQualMelhorDescontoProCliente(cliente, novoPedido);

            pedido.calcularEsetarValorTotal(desconto);

            if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO && pedido.getValorTotal().compareTo(cliente.getLimiteDeCredito()) > 0)
                throw new ClienteNaoPossuiLimiteSuficienteException(cliente.getLimiteDeCredito().toString());

            return pedidoMapper.toDTO(pedidoRepository.save(pedido));
             
    }

    public List<PedidoListarResponse> listar(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidoMapper.toListarDTO(pedidos);
    }

    public List<PedidoListarResponse> listarPedidosVencidosEmAberto() {
        return pedidoMapper.toListarDTO(pedidoRepository.findByDataVencimentoBeforeAndStatusPedido(LocalDateTime.now(), StatusPedido.PENDENTE));
    }

    public PedidoDetailResponse buscarPedidoPorId(Long id) {
        return pedidoMapper.toDetailDTO(pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFoundException(id)));
    }


    public List<ItemPedido> criarItemPedidos(PedidoCriarRequest novoPedido, Pedido pedido) {
        
        HashMap<Long, Long> codigoProdutos = novoPedido.itensPedido().stream().collect(Collectors.toMap(ItemPedidoCriarRequest::codigoIdProduto, ItemPedidoCriarRequest::quantidade, (existing, replacement) -> replacement, HashMap::new));
            List<Produto> produtos = produtoRepository.findAllById(codigoProdutos.keySet());
            Map<Long, Produto> produtosPorId = produtos.stream().collect(Collectors.toMap(Produto::getId, Function.identity()));

            if (produtos.size() != codigoProdutos.size())
                throw new NoValidProdutosForThisPedidoException();

            List<ItemPedido> itensPedidos = new ArrayList<>();

            for (ItemPedidoCriarRequest i : novoPedido.itensPedido()) {
             
                Produto produto = produtosPorId.get(i.codigoIdProduto());

                produto.baixarEstoque(i.quantidade());
                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setProduto(produto);
                item.setQuantidade(i.quantidade());
                item.calcularValorItem();

                itensPedidos.add(item);

            }

            return itensPedidos;
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

