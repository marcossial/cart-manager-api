package com.marcossial.cartmanager.config;

import com.marcossial.cartmanager.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioAuthenticationFilter usuarioAuthenticationFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          UsuarioAuthenticationFilter usuarioAuthenticationFilter,
                          ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.usuarioAuthenticationFilter = usuarioAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    public static final String[] ENDPOINTS_SEM_AUTENTICACAO = {
            "/api/usuarios/login",
            "/api/usuarios"
    };

    public static final String[] ENDPOINTS_COM_AUTENTICACAO = {
            "/api/usuarios/test"
    };

    public static final String[] ENDPOINTS_USUARIO = {
            "/api/cronograma/**"
    };

    public static final String[] ENDPOINTS_ADMIN = {
            "/api/professores/**",
            "/api/turmas/**",
            "/api/carrinhos/**",
            "/api/agendamentos/**",
            "/api/aulas/**",
            "/api/cronograma-padrao/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://equipamentos-ernesta.vercel.app/"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        // 401
                        .authenticationEntryPoint(((request, response, authException) -> {
                            throw authException;
                        }))
                        // 403
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            throw accessDeniedException;
                        })
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(ENDPOINTS_SEM_AUTENTICACAO).permitAll()
                        .requestMatchers(ENDPOINTS_COM_AUTENTICACAO).authenticated()
                        .requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMIN")
                        .requestMatchers(ENDPOINTS_USUARIO).hasAnyRole("ADMIN", "USUARIO")
                        .anyRequest().denyAll()
                )
                .addFilterBefore(usuarioAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
