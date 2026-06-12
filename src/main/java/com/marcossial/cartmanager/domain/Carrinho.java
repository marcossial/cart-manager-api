package com.marcossial.cartmanager.domain;

import com.marcossial.cartmanager.domain.enums.StatusCarrinho;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "carrinhos")
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", unique = true, nullable = false, length = 20)
    private String nome;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'DISPONIVEL'")
    @Column(name = "status", nullable = false, length = 20)
    private StatusCarrinho status;

    public Carrinho() { }

    public Carrinho(Integer id, String nome, StatusCarrinho status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusCarrinho getStatus() {
        return status;
    }

    public void setStatus(StatusCarrinho status) {
        this.status = status;
    }
}
