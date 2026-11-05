package com.nexus.backend.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import com.nexus.backend.enums.ClienteCategoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
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

    @CPF
    private String cpf;

    private String telefone;

    @Column( unique = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private ClienteCategoria categoria;
}
