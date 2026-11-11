package com.nexus.backend.entity;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Builder 
@Entity
public class ItemPedido {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    private Produto produto;

    private BigDecimal quantidade;

    private BigDecimal valorItem;

    public void calcularValorItem(){
        valorItem = produto.getPreco().multiply(quantidade);
    }
    
}


