package com.nexus.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexus.backend.entity.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    boolean existsByCnpj(String cnpj);

    Optional<Cliente> findByCnpj(String cnpj);

    boolean existsByEmail(String email);

    @Query(value = "select * from cliente where cnpj = :cnpj and excluido = true", nativeQuery = true)
    Optional<Cliente> findDesativadoByCnpj(@Param("cnpj") String cnpj);

    @Query(value = "select * from cliente where email = :email and excluido = true", nativeQuery = true)
    Optional<Cliente> findDesativadoByEmail(@Param("email") String email);

    @Query(value = "select * from cliente where id = :id", nativeQuery = true)
    Optional<Cliente> findByIdIncludingDeleted(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update cliente set excluido = false where id = :id", nativeQuery = true)
    void reativar(@Param("id") Long id);
}
