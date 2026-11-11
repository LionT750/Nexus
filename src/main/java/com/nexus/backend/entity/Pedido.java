package com.nexus.backend.entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;

import com.nexus.backend.enums.ModalidadePagamento;
import com.nexus.backend.enums.StatusPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter
@Builder 
@Entity
@SoftDelete (columnName = "excluido")
public class Pedido {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Min(0)
    private BigDecimal valorTotal;

    @CreationTimestamp 
    private LocalDate dataCriacao;

    private LocalDate dataVencimento;

    @Enumerated (EnumType.STRING)
    private StatusPedido statusPedido;

    @Enumerated (EnumType.STRING)
    private ModalidadePagamento modalidadePagamento;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne 
    Colaborador colaborador;

    @OneToMany (mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itemsPedido;


    public void adicionarItem(ItemPedido itemPedido) {
        itemsPedido.add(itemPedido);
        return;
    }

    public void calcularEsetarValorTotal(BigDecimal descontoEmDecimal) {
        valorTotal =  BigDecimal.ZERO;

        for (ItemPedido i : itemsPedido){
            valorTotal = valorTotal.add(i.getValorItem());
        }

        valorTotal = valorTotal.multiply((BigDecimal.ONE.subtract(descontoEmDecimal))).setScale(2, RoundingMode.HALF_UP);
        setValorTotal(valorTotal);

    }
    
}

