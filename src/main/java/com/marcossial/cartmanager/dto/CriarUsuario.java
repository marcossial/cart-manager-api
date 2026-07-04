package com.marcossial.cartmanager.dto;

import com.marcossial.cartmanager.domain.enums.Perfil;

public record CriarUsuario(
        String email,
        String senha,
        Perfil perfil
) {}
