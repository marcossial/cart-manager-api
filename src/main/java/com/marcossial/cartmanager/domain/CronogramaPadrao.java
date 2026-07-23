package com.marcossial.cartmanager.domain;

import com.marcossial.cartmanager.domain.enums.DiaSemana;
import com.marcossial.cartmanager.domain.enums.TipoSemana;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cronograma_padrao")
public class CronogramaPadrao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false, length = 15)
    private DiaSemana diaSemana;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_semana", nullable = false, length = 15)
    private TipoSemana tipoSemana = TipoSemana.AMBAS;

    @ManyToOne
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    public CronogramaPadrao() { }

    public CronogramaPadrao(Integer id, DiaSemana diaSemana, TipoSemana tipoSemana, Carrinho carrinho, Professor professor, Turma turma, Aula aula) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.tipoSemana = tipoSemana != null ? tipoSemana : TipoSemana.AMBAS;
        this.carrinho = carrinho;
        this.professor = professor;
        this.turma = turma;
        this.aula = aula;
    }

    public Integer getId() {
        return id;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public TipoSemana getTipoSemana() {
        return tipoSemana;
    }

    public void setTipoSemana(TipoSemana tipoSemana) {
        this.tipoSemana = tipoSemana;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
}
