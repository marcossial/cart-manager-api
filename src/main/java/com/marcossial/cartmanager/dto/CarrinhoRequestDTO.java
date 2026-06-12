package com.marcossial.cartmanager.dto;

import com.marcossial.cartmanager.domain.enums.StatusCarrinho;

public record CarrinhoRequestDTO(
    String nome,
    StatusCarrinho status
) {}
