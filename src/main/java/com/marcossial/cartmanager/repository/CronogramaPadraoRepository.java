package com.marcossial.cartmanager.repository;

import com.marcossial.cartmanager.domain.Aula;
import com.marcossial.cartmanager.domain.Carrinho;
import com.marcossial.cartmanager.domain.CronogramaPadrao;
import com.marcossial.cartmanager.domain.enums.DiaSemana;
import com.marcossial.cartmanager.domain.enums.TipoSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CronogramaPadraoRepository extends JpaRepository<CronogramaPadrao, Integer> {
    boolean existsByDiaSemanaAndAulaAndCarrinho(DiaSemana diaSemana, Aula aula, Carrinho carrinho);
    List<CronogramaPadrao> findByDiaSemanaAndTipoSemanaIn(DiaSemana diaSemana, Collection<TipoSemana> tiposSemana);
    List<CronogramaPadrao> findByCarrinhoAndDiaSemanaAndTipoSemanaIn(Carrinho carrinho, DiaSemana diaSemana, Collection<TipoSemana> tiposSemana);
    List<CronogramaPadrao> findByDiaSemana(DiaSemana diaSemana);
    List<CronogramaPadrao> findByAula(Aula aula);
    List<CronogramaPadrao> findByCarrinho(Carrinho carrinho);
}
