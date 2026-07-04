package com.marcossial.cartmanager.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.marcossial.cartmanager.repository.UsuarioRepository;
import com.marcossial.cartmanager.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UsuarioAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioAuthenticationFilter(JwtTokenService jwtTokenService, UsuarioRepository usuarioRepository) {
        this.jwtTokenService = jwtTokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().equals("/api/usuarios/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                usuarioRepository.findByEmail(subject).ifPresent(user -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleExceptionManually(response, request, e);
        }
    }

    private void handleExceptionManually(HttpServletResponse response, HttpServletRequest request, Exception e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format("{\"detail\": \"%s\", \"status\": 401, \"path\": \"%s\"}",
                "Token inválido ou não fornecido.", request.getRequestURI());
        response.getWriter().write(json);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
