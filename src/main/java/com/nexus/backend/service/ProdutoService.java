package com.nexus.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.backend.dtos.ItemPedidoListarAlugueisResponse;
import com.nexus.backend.dtos.ProdutoAdicionarEstoqueRequest;
import com.nexus.backend.dtos.ProdutoAdicionarEstoqueResponse;
import com.nexus.backend.dtos.ProdutoCriarRequest;
import com.nexus.backend.dtos.ProdutoCriarResponse;
import com.nexus.backend.dtos.ProdutoDeletarResponse;
import com.nexus.backend.dtos.ProdutoEditarRequest;
import com.nexus.backend.dtos.ProdutoEditarResponse;
import com.nexus.backend.dtos.ProdutoListarResponse;
import com.nexus.backend.entity.ItemPedido;
import com.nexus.backend.entity.Produto;
import com.nexus.backend.exceptions.ProdutoNotFoundException;
import com.nexus.backend.exceptions.QuantidadeMinimaParaAdicionarException;
import com.nexus.backend.mappers.ItemPedidoMapper;
import com.nexus.backend.mappers.ProdutoMapper;
import com.nexus.backend.repository.ItemPedidoRepository;
import com.nexus.backend.repository.ProdutoRepository;

import jakarta.transaction.Transactional;
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

    @Transactional 
    public ProdutoAdicionarEstoqueResponse adicionarEstoque(ProdutoAdicionarEstoqueRequest dto) {
        Produto produto = produtoRepository.findById(dto.idProduto()).orElseThrow(() -> new ProdutoNotFoundException(dto.idProduto()));
        if (dto.qtdAdicionar() < 1)
            throw new QuantidadeMinimaParaAdicionarException();
        produto.reporEstoque(dto.qtdAdicionar());
        return produtoMapper.toEstoqueDTO(produto);
    }

    public List<ProdutoListarResponse> listar(){
        List<Produto> produtos = produtoRepository.findAll();
        return produtoMapper.toDTO(produtos);
    }

    public List<ItemPedidoListarAlugueisResponse> listarAlugados() {
        List<ItemPedido> itensPedidosAluguel = itemPedidoRepository.findByisAluguelTrue();
        return itemPedidoMapper.toAluguelDTO(itensPedidosAluguel);
    }

    public ProdutoEditarResponse editar(Long idProduto, ProdutoEditarRequest dto) {
        Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new ProdutoNotFoundException(idProduto));

        produtoMapper.editarEntity(dto, produto);
        produtoRepository.save(produto);

        return produtoMapper.toEditarDTO(produto);
    }

    @Transactional
    public ProdutoDeletarResponse deletar(Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new ProdutoNotFoundException(idProduto));

        produtoRepository.delete(produto);

        return produtoMapper.toDeletarDTO(produto);
    }
  
}
