package com.nexus.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexus.backend.entity.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long>{
    boolean existsByName(String name);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Optional<Colaborador> findByName(String name);

    @Query(value = "select * from colaborador where name = :name and excluido = true", nativeQuery = true)
    Optional<Colaborador> findDesativadoByName(@Param("name") String name);

    @Query(value = "select * from colaborador where email = :email and excluido = true", nativeQuery = true)
    Optional<Colaborador> findDesativadoByEmail(@Param("email") String email);

    @Query(value = "select * from colaborador where id = :id", nativeQuery = true)
    Optional<Colaborador> findByIdIncludingDeleted(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update colaborador set excluido = false where id = :id", nativeQuery = true)
    void reativar(@Param("id") Long id);
}
