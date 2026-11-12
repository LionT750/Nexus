package com.nexus.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.backend.dtos.ItemPedidoListarAlugueisResponse;
import com.nexus.backend.dtos.ProdutoCriarRequest;
import com.nexus.backend.dtos.ProdutoCriarResponse;
import com.nexus.backend.dtos.ProdutoListarResponse;
import com.nexus.backend.entity.ItemPedido;
import com.nexus.backend.entity.Produto;
import com.nexus.backend.mappers.ItemPedidoMapper;
import com.nexus.backend.mappers.ProdutoMapper;
import com.nexus.backend.repository.ItemPedidoRepository;
import com.nexus.backend.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoMapper produtoMapper;
    private final ItemPedidoMapper itemPedidoMapper;


    public ProdutoCriarResponse criar(ProdutoCriarRequest novoProduto) {
        Produto Produto = produtoMapper.toEntity(novoProduto);
        Produto persistedProduto = produtoRepository.save(Produto);
        return produtoMapper.toDTO(persistedProduto);
        
    }

    public List<ProdutoListarResponse> listar(){
        List<Produto> produtos = produtoRepository.findAll();
        return produtoMapper.toDTO(produtos);
    }

    public List<ItemPedidoListarAlugueisResponse> listarAlugados() {
        List<ItemPedido> itensPedidosAluguel = itemPedidoRepository.findByDataVencimentoAluguelIsNotNull();
        return itemPedidoMapper.toAluguelDTO(itensPedidosAluguel);
    }
  
}
