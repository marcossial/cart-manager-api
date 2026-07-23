package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Usuario;
import com.marcossial.cartmanager.dto.CriarUsuario;
import com.marcossial.cartmanager.dto.LoginUsuario;
import com.marcossial.cartmanager.dto.RecoveryJwtTokenDto;
import com.marcossial.cartmanager.exception.RegraDeNegocioException;
import com.marcossial.cartmanager.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RecoveryJwtTokenDto autenticarUsuario(LoginUsuario dto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Usuario usuario = (Usuario) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(usuario), usuario.getPerfil().name());
    }

    public RecoveryJwtTokenDto criarUsuario(CriarUsuario dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RegraDeNegocioException("Email ja cadastrado");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(dto.email());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
        novoUsuario.setPerfil(dto.perfil());

        usuarioRepository.save(novoUsuario);

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(novoUsuario), novoUsuario.getPerfil().name());
    }
}
