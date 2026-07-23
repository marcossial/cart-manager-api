package com.marcossial.cartmanager.dto;

import com.marcossial.cartmanager.domain.Reserva;
import com.marcossial.cartmanager.domain.enums.TipoSemana;
import com.marcossial.cartmanager.dto.enums.OrigemAulaConsolidada;

import java.time.LocalTime;

public record CronogramaResponseDTO(
        Integer numeroAula,
        LocalTime horaInicio,
        LocalTime horaFim,
        TipoSemana tipoSemana,
        Reserva[] reservas,
        OrigemAulaConsolidada origem
) {}
