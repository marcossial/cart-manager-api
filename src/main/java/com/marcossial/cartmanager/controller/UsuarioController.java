package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.dto.CriarUsuario;
import com.marcossial.cartmanager.dto.LoginUsuario;
import com.marcossial.cartmanager.dto.RecoveryJwtTokenDto;
import com.marcossial.cartmanager.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> autenticarUsuario(@RequestBody LoginUsuario dto) {
        RecoveryJwtTokenDto token = usuarioService.autenticarUsuario(dto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RecoveryJwtTokenDto> criarUsuario(@RequestBody CriarUsuario dto) {
        RecoveryJwtTokenDto token = usuarioService.criarUsuario(dto);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testeAutenticacao() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }
}
