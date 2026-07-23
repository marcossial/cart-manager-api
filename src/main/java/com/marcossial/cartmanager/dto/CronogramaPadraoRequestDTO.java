package com.marcossial.cartmanager.dto;

import com.marcossial.cartmanager.domain.enums.DiaSemana;
import com.marcossial.cartmanager.domain.enums.TipoSemana;

public record CronogramaPadraoRequestDTO(
    DiaSemana diaSemana,
    TipoSemana tipoSemana,
    Integer aulaId,
    Integer carrinhoId,
    Integer professorId,
    Integer turmaId
) {}
