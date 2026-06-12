package com.marcossial.cartmanager.repository;

import com.marcossial.cartmanager.domain.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {
    Optional<Aula> findByNumeroAula(Short numeroAula);
}
