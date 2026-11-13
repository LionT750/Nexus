package com.nexus.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.SoftDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor 
@NoArgsConstructor 
@Getter
@Setter 
@Builder 
@Entity
@SoftDelete (columnName = "excluido")
public class ItemPedido {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    private Produto produto;

    @ManyToOne 
    private Pedido pedido;

    @Min(0)
    private Long quantidade;

    private BigDecimal valorItem;

    @NotNull 
    private boolean isAluguel;

    private LocalDate dataVencimentoAluguel;

    public void calcularValorItem(){
        if (isAluguel == true)
            valorItem = produto.getPrecoVenda().multiply(BigDecimal.valueOf(quantidade));
        else
            valorItem = produto.getPrecoAluguel().multiply(BigDecimal.valueOf(quantidade));
    }
    
}


