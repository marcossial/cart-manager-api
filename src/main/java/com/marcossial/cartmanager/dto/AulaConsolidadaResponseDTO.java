package com.marcossial.cartmanager.dto;

import com.marcossial.cartmanager.dto.enums.OrigemAulaConsolidada;

import java.time.LocalTime;

public record AulaConsolidadaResponseDTO(
        Integer numeroAula,
        LocalTime horaInicio,
        LocalTime horaFim,
        String carrinhoNome,
        String professorNome,
        String turmaNome,
        OrigemAulaConsolidada origem
) {}
