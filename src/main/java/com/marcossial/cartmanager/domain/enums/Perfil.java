package com.marcossial.cartmanager.domain.enums;

public enum Perfil {
    ADMIN("ROLE_ADMIN"),
    USUARIO("ROLE_USUARIO");

    private final String role;

    Perfil(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
