package com.marcossial.cartmanager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalTime;

@Entity
@Table(name = "aulas")
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero_aula", nullable = false)
    private short numeroAula;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fim")
    private LocalTime horaFim;

    public Aula() { }

    public Aula(Integer id, short numeroAula, LocalTime horaInicio, LocalTime horaFim) {
        this.id = id;
        this.numeroAula = numeroAula;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public Integer getId() {
        return id;
    }

    public short getNumeroAula() {
        return numeroAula;
    }

    public void setNumeroAula(short numeroAula) {
        this.numeroAula = numeroAula;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
}
