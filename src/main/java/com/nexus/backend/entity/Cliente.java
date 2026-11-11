package com.nexus.backend.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import com.nexus.backend.enums.ClienteClassificao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
public class Cliente {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 60)
    private String name;

    @CNPJ(format = CNPJ.Format.ALPHANUMERIC) 
    @Column (nullable = false, unique = true)
    private String cnpj;

    private String telefone;

    @Column( unique = true)
    @Email
    private String email;

    @Min(0)
    private BigDecimal limiteDeCredito;

    @Enumerated(EnumType.STRING)
    private ClienteClassificao classificacao;
}
