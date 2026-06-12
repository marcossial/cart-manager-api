package com.marcossial.cartmanager.dto;

import com.marcossial.cartmanager.domain.enums.DiaSemana;

public record CronogramaPadraoRequestDTO(
    DiaSemana diaSemana,
    Integer aulaId,
    Integer carrinhoId,
    Integer professorId,
    Integer turmaId
) {}
