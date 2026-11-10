package com.nexus.backend.entity;

import java.math.BigDecimal;

import com.nexus.backend.enums.StatusProduto;
import com.nexus.backend.exceptions.EstoqueInsuficienteException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Builder 
@Entity
public class Produto {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    @Min(0)
    @Column
    private Long estoque;

    @Min(0)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private StatusProduto statusProduto;


    public void baixarEstoque(Long quantidadeParaBaixar){
        if (quantidadeParaBaixar - estoque < 0)
            throw new EstoqueInsuficienteException(estoque, quantidadeParaBaixar);

        estoque -= quantidadeParaBaixar;
    }

    
}
