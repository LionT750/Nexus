package com.nexus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexus.backend.entity.ItemPedido;
import com.nexus.backend.enums.StatusPedido;
import java.util.List;


public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
    List<ItemPedido> findByisAluguelTrue();

    boolean existsByProdutoIdAndPedidoStatusPedido(Long idProduto, StatusPedido statusPedido);

    @Query("select (count(i) > 0) from ItemPedido i where i.produto.id = :idProduto and i.isAluguel = true and i.dataDevolucao is null")
    boolean existsAluguelNaoDevolvidoByProdutoId(@Param("idProduto") Long idProduto);
}
