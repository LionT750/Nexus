package com.nexus.backend.entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.nexus.backend.enums.StatusPedido;

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
import lombok.NoArgsConstructor;


@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Builder 
@Entity
public class Pedido {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne Colaborador colaborador;

    @Min(0)
    private BigDecimal valorTotal;

    @CreationTimestamp 
    private LocalDate dataCriacao;

    private LocalDate dataVencimento;

    @Enumerated (EnumType.STRING)
    private StatusPedido statusPedido;

    @OneToMany (mappedBy = "id")
    private List<Produto> produto;

    
}

