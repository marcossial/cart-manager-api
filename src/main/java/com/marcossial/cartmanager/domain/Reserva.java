package com.marcossial.cartmanager.domain;

public class Reserva {
    private String equipamento;
    private String turma;
    private String professor;

    public Reserva(String equipamento, String turma, String professor) {
        this.equipamento = equipamento;
        this.turma = turma;
        this.professor = professor;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
