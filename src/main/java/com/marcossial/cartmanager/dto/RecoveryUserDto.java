package com.marcossial.cartmanager.dto;

import com.marcossial.cartmanager.domain.enums.Perfil;

import java.util.List;

public record RecoveryUserDto(
        Long id,
        String email,
        List<Perfil> perfis
) {}
