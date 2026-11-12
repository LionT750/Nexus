package com.nexus.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus.backend.dtos.ItemPedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarResponse;
import com.nexus.backend.dtos.PedidoDetailResponse;
import com.nexus.backend.dtos.PedidoListarResponse;
import com.nexus.backend.dtos.PedidoPorClienteRequest;
import com.nexus.backend.dtos.ResolverSolicitacaoDescontoRequest;
import com.nexus.backend.dtos.ResolverSolicitacaoDescontoResponse;
import com.nexus.backend.dtos.SolicitacaoDescontoListarResponse;
import com.nexus.backend.entity.Cliente;
import com.nexus.backend.entity.Colaborador;
import com.nexus.backend.entity.ItemPedido;
import com.nexus.backend.entity.Pedido;
import com.nexus.backend.entity.Produto;
import com.nexus.backend.enums.ClienteClassificao;
import com.nexus.backend.enums.ModalidadePagamento;
import com.nexus.backend.enums.StatusAprovacaoDesconto;
import com.nexus.backend.enums.StatusPedido;
import com.nexus.backend.exceptions.ClienteNaoPossuiLimiteSuficienteException;
import com.nexus.backend.exceptions.ClienteNotFoundException;
import com.nexus.backend.exceptions.ColaboradorNotFoundException;
import com.nexus.backend.exceptions.DataVencimentoObrigatoriaException;
import com.nexus.backend.exceptions.NoValidProdutosForThisPedidoException;
import com.nexus.backend.exceptions.PedidoNaoPodeSerConcluidoException;
import com.nexus.backend.exceptions.PedidoNotFoundException;
import com.nexus.backend.exceptions.SolicitacaoDescontoNaoPodeSerResolvidaException;
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
            
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setDataVencimento(novoPedido.dataVencimento());
            pedido.setDescricao(novoPedido.descricao());
            
            List<ItemPedido> itensPedidos = criarItemPedidos(novoPedido, pedido);

            pedido.setItemsPedido(itensPedidos);
            pedido.setModalidadePagamento(novoPedido.modalidadePagamento());
            pedido.setStatusPedido(StatusPedido.PENDENTE);
            pedido.setDescontoSolicitado(novoPedido.descontoSolicitado());
            pedido.setDescontoAutorizado(BigDecimal.valueOf(0.0));
            
            if (novoPedido.descontoSolicitado().compareTo(BigDecimal.ZERO) == 0 && cliente.getClassificacao() == ClienteClassificao.NORMAL)
                pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.NAO_SOLICITADO);
            else if (novoPedido.descontoSolicitado().compareTo(BigDecimal.valueOf(0.05)) <= 0 && cliente.getClassificacao() == ClienteClassificao.VIP)
            {
                pedido.setDescontoAutorizado(BigDecimal.valueOf(0.05));
                pedido.setDataAtualizacaoDesconto(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
                pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.APROVADO_AUTOMATICAMENTE);
            }
            else
                pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.PENDENTE);

            pedido.calcularEsetarValorTotal(pedido.getDescontoAutorizado());

            if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO)
            {
                if (pedido.getDataVencimento() == null)
                    throw new DataVencimentoObrigatoriaException();

                if (pedido.getValorTotal().compareTo(cliente.getLimiteDeCredito()) > 0)
                    throw new ClienteNaoPossuiLimiteSuficienteException(cliente.getLimiteDeCredito().toString());

                cliente.setLimiteDeCredito(cliente.getLimiteDeCredito().subtract(pedido.getValorTotal()));
            }

            validarPedidoNumEstadoValido(pedido);
            return pedidoMapper.toDTO(pedidoRepository.save(pedido));
             
    }

    public List<PedidoListarResponse> listar(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidoMapper.toListarDTO(pedidos);
    }

    public List<PedidoListarResponse> listarPedidosVencidosEmAberto() {
        return pedidoMapper.toListarDTO(pedidoRepository.findByDataVencimentoBeforeAndStatusPedido(LocalDate.now(), StatusPedido.PENDENTE));
    }

    public List<PedidoListarResponse> listarPedidosDoCliente(PedidoPorClienteRequest pedidoPorClienteRequest) {

        Map<String, String> camposDeIdentificacao = new LinkedHashMap<>();

        camposDeIdentificacao.put("idCliente", pedidoPorClienteRequest.idCliente() != null ? pedidoPorClienteRequest.idCliente().toString() : null);

        camposDeIdentificacao.put("CNPJcliente", pedidoPorClienteRequest.CNPJcliente());

        camposDeIdentificacao.put("emailCliente", pedidoPorClienteRequest.emailCliente());

        camposDeIdentificacao.values().removeIf(Objects::isNull);

        for (Map.Entry<String, String> campo : camposDeIdentificacao.entrySet()) {

            String nomePropriedade = campo.getKey();
            String valor = campo.getValue();

            switch (nomePropriedade) {
                case "idCliente":
                    if (!clienteRepository.existsById(pedidoPorClienteRequest.idCliente()))
                        throw new ClienteNotFoundException(pedidoPorClienteRequest.idCliente());
                    return pedidoMapper.toListarDTO(pedidoRepository.findByClienteId(pedidoPorClienteRequest.idCliente()));
    
                case "CNPJcliente":
                    if (!clienteRepository.existsByCnpj(valor))
                        throw new ClienteNotFoundException(valor);
                    return pedidoMapper.toListarDTO(pedidoRepository.findByClienteCnpj(valor));

                case "emailCliente":
                    if (!clienteRepository.existsByEmail(valor))
                        throw new ClienteNotFoundException("email", valor);
                    return pedidoMapper.toListarDTO(pedidoRepository.findByClienteEmail(valor));
            }
        }


        if (!clienteRepository.existsById(pedidoPorClienteRequest.idCliente()))
            throw new ClienteNotFoundException(pedidoPorClienteRequest.idCliente());

        return pedidoMapper.toListarDTO(pedidoRepository.findByClienteId(pedidoPorClienteRequest.idCliente()));
    }

    public List<SolicitacaoDescontoListarResponse> listarPedidosComSolicitacaoDescontoAberta() {
        return pedidoMapper.toListarDescontoDTO(pedidoRepository.findByStatusPedidoAndStatusAprovacaoDesconto(StatusPedido.PENDENTE, StatusAprovacaoDesconto.PENDENTE)); 
    }

    @Transactional
    public ResolverSolicitacaoDescontoResponse resolverSolicitacaoDesconto(Long idPedido, ResolverSolicitacaoDescontoRequest resolverSolicitacaoDescontoRequest) {

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException(idPedido));
        Colaborador colaborador = colaboradorRepository.findById(resolverSolicitacaoDescontoRequest.idColaborador()).orElseThrow(() -> new ColaboradorNotFoundException(resolverSolicitacaoDescontoRequest.idColaborador().toString()));

        if (pedido.getStatusAprovacaoDesconto() != StatusAprovacaoDesconto.PENDENTE)
            throw new SolicitacaoDescontoNaoPodeSerResolvidaException("A solicitacao de desconto nao pode ser resolvida pois nao esta pendente, status atual: " + pedido.getStatusAprovacaoDesconto());

        if (resolverSolicitacaoDescontoRequest.statusAprovacaoDesconto() != StatusAprovacaoDesconto.APROVADO
        && resolverSolicitacaoDescontoRequest.statusAprovacaoDesconto() != StatusAprovacaoDesconto.REPROVADO)
            throw new SolicitacaoDescontoNaoPodeSerResolvidaException("A solicitacao de desconto so pode ser resolvida como APROVADO ou REPROVADO");

        pedido.setColaborador(colaborador);
        pedido.setStatusAprovacaoDesconto(resolverSolicitacaoDescontoRequest.statusAprovacaoDesconto());
        pedido.setDataAtualizacaoDesconto(LocalDate.now(ZoneId.of("America/Sao_Paulo")));

        if (pedido.getStatusAprovacaoDesconto() == StatusAprovacaoDesconto.APROVADO) {
            BigDecimal valorTotalAntesDoDesconto = pedido.getValorTotal();

            pedido.setDescontoAutorizado(pedido.getDescontoSolicitado());
            pedido.calcularEsetarValorTotal(pedido.getDescontoAutorizado());

            if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO) {
                Cliente cliente = pedido.getCliente();
                cliente.setLimiteDeCredito(cliente.getLimiteDeCredito().add(valorTotalAntesDoDesconto.subtract(pedido.getValorTotal())));
            }
        }

        validarPedidoNumEstadoValido(pedido);
        return pedidoMapper.toResolverDescontoDTO(pedidoRepository.save(pedido));
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
                if (i.dataVencimentoAluguel() != null)
                    item.setDataVencimentoAluguel(i.dataVencimentoAluguel());
                else
                    item.setDataVencimentoAluguel(null);
                item.calcularValorItem();

                itensPedidos.add(item);

            }

            return itensPedidos;
    }
  

    public void validarPedidoNumEstadoValido(Pedido pedido) {
        if (pedido.getStatusPedido() != StatusPedido.PENDENTE && pedido.getStatusAprovacaoDesconto() == (StatusAprovacaoDesconto.PENDENTE))
            throw new PedidoNaoPodeSerConcluidoException("O pedido nao pode ser concluido pois contem uma solicitacao de desconto pendente de autorizacao");

        if ((pedido.getStatusAprovacaoDesconto() == StatusAprovacaoDesconto.APROVADO
        || pedido.getStatusAprovacaoDesconto() == StatusAprovacaoDesconto.REPROVADO) 
        && pedido.getColaborador() == null)
            throw new PedidoNaoPodeSerConcluidoException("O pedido nao pode ser concluido pois o desconto aprovado/reprovado nao possui um colaborador especificado");

    }

}

