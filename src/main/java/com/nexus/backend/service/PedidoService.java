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
import com.nexus.backend.dtos.PedidoDeletarResponse;
import com.nexus.backend.dtos.PedidoDetailResponse;
import com.nexus.backend.dtos.PedidoDevolverResponse;
import com.nexus.backend.dtos.PedidoEditarRequest;
import com.nexus.backend.dtos.PedidoFinalizarResponse;
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
import com.nexus.backend.exceptions.AluguelMustHaveAdataVencimentoException;
import com.nexus.backend.exceptions.AluguelNaoPodeSerDevolvidoException;
import com.nexus.backend.exceptions.ClienteInadimplenteNaoPodeAbrirPrazoException;
import com.nexus.backend.exceptions.ClienteNaoPossuiLimiteSuficienteException;
import com.nexus.backend.exceptions.ClienteNotFoundException;
import com.nexus.backend.exceptions.ColaboradorNotFoundException;
import com.nexus.backend.exceptions.DataVencimentoObrigatoriaException;
import com.nexus.backend.exceptions.NoValidProdutosForThisPedidoException;
import com.nexus.backend.exceptions.PedidoJaPagoNaoPodeSerModificadoException;
import com.nexus.backend.exceptions.PedidoNaoPodeSerConcluidoException;
import com.nexus.backend.exceptions.PedidoNaoPodeSerDeletadoException;
import com.nexus.backend.exceptions.PedidoNaoPodeSerFinalizadoException;
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
            boolean ehClienteInadiplente = validarSeClienteEhInadiplente(cliente);

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setDataVencimento(novoPedido.dataVencimento());
            pedido.setDescricao(novoPedido.descricao());
            
            List<ItemPedido> itensPedidos = criarItemPedidos(novoPedido, pedido);

            pedido.setItemsPedido(itensPedidos);
            pedido.setModalidadePagamento(novoPedido.modalidadePagamento());
            pedido.setStatusPedido(StatusPedido.PENDENTE);

            if (!ehClienteInadiplente)
            {
                pedido.setDescontoSolicitado(gerarDescontoSolicitadoBaseadoClassificacaoCliente(novoPedido, cliente));
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
            }

            else
            {
                pedido.setDescontoSolicitado(gerarDescontoSolicitadoBaseadoClassificacaoCliente(novoPedido, cliente));
                pedido.setDescontoAutorizado(BigDecimal.valueOf(0.0));
                pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.REPROVADO_INADIPLENCIA);
                pedido.calcularEsetarValorTotal(pedido.getDescontoAutorizado());
            }


            if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO && !ehClienteInadiplente)
            {
                if (pedido.getDataVencimento() == null)
                    throw new DataVencimentoObrigatoriaException();

                if (pedido.getValorTotal().compareTo(cliente.getLimiteDeCredito()) > 0)
                    throw new ClienteNaoPossuiLimiteSuficienteException(cliente.getLimiteDeCredito().toString());


                cliente.setLimiteDeCredito(cliente.getLimiteDeCredito().subtract(pedido.getValorTotal()));
            }

            else if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO && ehClienteInadiplente) {
                throw new ClienteInadimplenteNaoPodeAbrirPrazoException();

            }

            validarPedidoNumEstadoValido(pedido);
            return pedidoMapper.toDTO(pedidoRepository.save(pedido));
             
    }

    @Transactional 
    public PedidoDetailResponse editarPedido(Long idPedido, PedidoEditarRequest dto) {
           Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException(idPedido));

           if (pedido.getStatusPedido() != StatusPedido.PENDENTE)
            throw new PedidoJaPagoNaoPodeSerModificadoException();

           Cliente cliente = pedido.getCliente();
           boolean eraAPrazo = pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO;
           BigDecimal valorTotalAnterior = pedido.getValorTotal();
           ModalidadePagamento modalidade = dto.modalidadePagamento() == null
                   ? pedido.getModalidadePagamento()
                   : dto.modalidadePagamento();

           if (dto.dataVencimento() != null)
               pedido.setDataVencimento(dto.dataVencimento());

           if (modalidade == ModalidadePagamento.A_PRAZO && pedido.getDataVencimento() == null)
               throw new DataVencimentoObrigatoriaException();

           if (modalidade == ModalidadePagamento.A_PRAZO && !eraAPrazo && validarSeClienteEhInadiplente(cliente))
               throw new ClienteInadimplenteNaoPodeAbrirPrazoException();

           if (dto.descricao() != null)
               pedido.setDescricao(dto.descricao());

           if (dto.itensPedido() != null) {
               if (dto.itensPedido().isEmpty())
                   throw new NoValidProdutosForThisPedidoException();

               devolverEstoqueItens(pedido.getItemsPedido());

               List<ItemPedido> novosItens = criarItemPedidos(dto, pedido);
               pedido.getItemsPedido().clear();
               pedido.getItemsPedido().addAll(novosItens);
           }

           pedido.setModalidadePagamento(modalidade);

           if (dto.descontoSolicitado() != null) {
               BigDecimal descontoSolicitado = gerarDescontoSolicitadoBaseadoClassificacaoCliente(dto.descontoSolicitado(), cliente);
               pedido.setDescontoSolicitado(descontoSolicitado);

               if (descontoSolicitado.compareTo(pedido.getDescontoAutorizado()) > 0) {
                   if (cliente.getClassificacao() == ClienteClassificao.VIP
                           && descontoSolicitado.compareTo(BigDecimal.valueOf(0.05)) <= 0
                           && pedido.getStatusAprovacaoDesconto() != StatusAprovacaoDesconto.REPROVADO_INADIPLENCIA) {
                       pedido.setDescontoAutorizado(BigDecimal.valueOf(0.05));
                       pedido.setDataAtualizacaoDesconto(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
                       pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.APROVADO_AUTOMATICAMENTE);
                   }
                   else if (pedido.getStatusAprovacaoDesconto() != StatusAprovacaoDesconto.REPROVADO_INADIPLENCIA) {
                       pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.PENDENTE);
                       pedido.setColaboradorDesconto(null);
                   }
               }

               else if (descontoSolicitado.compareTo(pedido.getDescontoAutorizado()) <= 0
                       && pedido.getStatusAprovacaoDesconto() == StatusAprovacaoDesconto.PENDENTE) {
                   pedido.setColaboradorDesconto(null);

                   if (descontoSolicitado.compareTo(BigDecimal.ZERO) == 0 && cliente.getClassificacao() == ClienteClassificao.NORMAL)
                       pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.NAO_SOLICITADO);

                   else if (cliente.getClassificacao() == ClienteClassificao.VIP && descontoSolicitado.compareTo(BigDecimal.valueOf(0.05)) <= 0) {
                       pedido.setDescontoAutorizado(BigDecimal.valueOf(0.05));
                       pedido.setDataAtualizacaoDesconto(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
                       pedido.setStatusAprovacaoDesconto(StatusAprovacaoDesconto.APROVADO_AUTOMATICAMENTE);
                   }
               }
           }

           pedido.calcularEsetarValorTotal(pedido.getDescontoAutorizado());

           if (eraAPrazo)
               cliente.setLimiteDeCredito(cliente.getLimiteDeCredito().add(valorTotalAnterior));

           if (modalidade == ModalidadePagamento.A_PRAZO) {
               if (pedido.getValorTotal().compareTo(cliente.getLimiteDeCredito()) > 0)
                   throw new ClienteNaoPossuiLimiteSuficienteException(cliente.getLimiteDeCredito().toString());

               cliente.setLimiteDeCredito(cliente.getLimiteDeCredito().subtract(pedido.getValorTotal()));
           }

           validarPedidoNumEstadoValido(pedido);
           return pedidoMapper.toDetailDTO(pedidoRepository.save(pedido));

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

        pedido.setColaboradorDesconto(colaborador);
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

         if (pedido.getStatusAprovacaoDesconto() == StatusAprovacaoDesconto.REPROVADO && pedido.getCliente().getClassificacao() == ClienteClassificao.VIP) {
            BigDecimal valorTotalAntesDoDesconto = pedido.getValorTotal();

            pedido.setDescontoAutorizado(BigDecimal.valueOf(0.05));
            pedido.calcularEsetarValorTotal(pedido.getDescontoAutorizado());

            if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO) {
                Cliente cliente = pedido.getCliente();
                cliente.setLimiteDeCredito(cliente.getLimiteDeCredito().add(valorTotalAntesDoDesconto.subtract(pedido.getValorTotal())));
            }
        }


        validarPedidoNumEstadoValido(pedido);
        return pedidoMapper.toResolverDescontoDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoFinalizarResponse finalizarPedido(Long idPedido) {

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException(idPedido));

        if (pedido.getStatusPedido() != StatusPedido.PENDENTE)
            throw new PedidoNaoPodeSerFinalizadoException("O pedido nao pode ser finalizado pois nao esta pendente, status atual: " + pedido.getStatusPedido());

        pedido.setStatusPedido(StatusPedido.PAGO);

        if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO) {
            Cliente cliente = pedido.getCliente();
            cliente.setLimiteDeCredito(cliente.getLimiteDeCredito().add(pedido.getValorTotal()));
        }

        validarPedidoNumEstadoValido(pedido);
        return pedidoMapper.toFinalizarDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoDevolverResponse devolverAlugueis(Long idPedido) {

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException(idPedido));

        List<ItemPedido> alugueis = pedido.getItemsPedido().stream().filter(ItemPedido::isAluguel).toList();

        if (alugueis.isEmpty())
            throw new AluguelNaoPodeSerDevolvidoException("O pedido nao possui itens de aluguel para devolver");

        List<ItemPedido> alugueisParaDevolver = alugueis.stream().filter(i -> i.getDataDevolucao() == null).toList();

        if (alugueisParaDevolver.isEmpty())
            throw new AluguelNaoPodeSerDevolvidoException("Os alugueis deste pedido ja foram devolvidos");

        for (ItemPedido item : alugueisParaDevolver) {
            item.getProduto().reporEstoque(item.getQuantidade());
            item.setDataDevolucao(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
        }

        validarPedidoNumEstadoValido(pedido);
        pedidoRepository.save(pedido);
        return pedidoMapper.toDevolverDTO(pedido, alugueisParaDevolver);
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
                if (i.isAluguel() == true)
                {
                    item.setAluguel(i.isAluguel());
                    if (i.dataVencimentoAluguel() == null)
                        throw new AluguelMustHaveAdataVencimentoException();
                    item.setDataVencimentoAluguel(i.dataVencimentoAluguel());
                }
                else
                    item.setDataVencimentoAluguel(null);
                item.calcularValorItem();

                itensPedidos.add(item);

            }

            return itensPedidos;
    }
  
    public List<ItemPedido> criarItemPedidos(PedidoEditarRequest novoPedido, Pedido pedido) {
        
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
                if (i.isAluguel() == true)
                {
                    item.setAluguel(i.isAluguel());
                    if (i.dataVencimentoAluguel() == null)
                        throw new AluguelMustHaveAdataVencimentoException();
                    item.setDataVencimentoAluguel(i.dataVencimentoAluguel());
                }
                else
                    item.setDataVencimentoAluguel(null);
                item.calcularValorItem();

                itensPedidos.add(item);

            }

            return itensPedidos;
    }


    @Transactional
    public PedidoDeletarResponse deletar(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException(idPedido));

        if (pedido.getStatusPedido() != StatusPedido.PENDENTE)
            throw new PedidoNaoPodeSerDeletadoException(idPedido, String.valueOf(pedido.getStatusPedido()));

        devolverEstoqueItens(pedido.getItemsPedido());

        if (pedido.getModalidadePagamento() == ModalidadePagamento.A_PRAZO)
            pedido.getCliente().setLimiteDeCredito(pedido.getCliente().getLimiteDeCredito().add(pedido.getValorTotal()));

        pedidoRepository.delete(pedido);

        return pedidoMapper.toDeletarDTO(pedido);
    }

    private void devolverEstoqueItens(List<ItemPedido> itens) {
        for (ItemPedido item : itens) {
            if (!(item.isAluguel() && item.getDataDevolucao() != null))
                item.getProduto().reporEstoque(item.getQuantidade());
        }
    }




    public void validarPedidoNumEstadoValido(Pedido pedido) {
        if (pedido.getStatusPedido() != StatusPedido.PENDENTE && pedido.getStatusAprovacaoDesconto() == (StatusAprovacaoDesconto.PENDENTE))
            throw new PedidoNaoPodeSerConcluidoException("O pedido nao pode ser concluido pois contem uma solicitacao de desconto pendente de autorizacao");

        if ((pedido.getStatusAprovacaoDesconto() == StatusAprovacaoDesconto.APROVADO
        || pedido.getStatusAprovacaoDesconto() == StatusAprovacaoDesconto.REPROVADO) 
        && pedido.getColaboradorDesconto() == null)
            throw new PedidoNaoPodeSerConcluidoException("O pedido nao pode ser concluido pois o desconto aprovado/reprovado nao possui um colaborador especificado");

}

    public boolean validarSeClienteEhInadiplente(Cliente cliente)  {
        return pedidoRepository.existsByDataVencimentoBeforeAndStatusPedidoAndClienteId(LocalDate.now(), StatusPedido.PENDENTE, cliente.getId());
    }

    public BigDecimal gerarDescontoSolicitadoBaseadoClassificacaoCliente(PedidoCriarRequest novoPedido, Cliente cliente) {
        return gerarDescontoSolicitadoBaseadoClassificacaoCliente(novoPedido.descontoSolicitado(), cliente);
    }

    public BigDecimal gerarDescontoSolicitadoBaseadoClassificacaoCliente(BigDecimal descontoSolicitado, Cliente cliente) {

        if (descontoSolicitado.compareTo(new BigDecimal(0.1)) < 0)
            return descontoSolicitado;

        else
            return cliente.getClassificacao() == ClienteClassificao.VIP ? descontoSolicitado : new BigDecimal(0.1);

    }

}

