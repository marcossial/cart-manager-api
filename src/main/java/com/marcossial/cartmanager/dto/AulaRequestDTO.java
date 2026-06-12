package com.marcossial.cartmanager.dto;

import java.time.LocalTime;

public record AulaRequestDTO(
    Short numeroAula,
    LocalTime horaInicio,
    LocalTime horaFim
) {}
