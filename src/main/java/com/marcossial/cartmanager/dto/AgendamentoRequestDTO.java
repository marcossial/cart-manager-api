package com.marcossial.cartmanager.dto;

import java.time.LocalDate;

public record AgendamentoRequestDTO(
        LocalDate data,
        Integer aulaId,
        Integer carrinhoId,
        Integer professorId,
        Integer turmaId
) {}
