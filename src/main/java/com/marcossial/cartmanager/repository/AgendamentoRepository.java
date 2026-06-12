package com.marcossial.cartmanager.repository;

import com.marcossial.cartmanager.domain.Agendamento;
import com.marcossial.cartmanager.domain.Aula;
import com.marcossial.cartmanager.domain.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    Optional<Agendamento> findByDataAndAulaAndCarrinho(LocalDate data, Aula aula, Carrinho carrinho);

    List<Agendamento> findByData(LocalDate data);
    List<Agendamento> findByDataAndCarrinho(LocalDate data, Carrinho carrinho);
}
