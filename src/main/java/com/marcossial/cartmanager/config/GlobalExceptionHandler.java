package com.marcossial.cartmanager.config;

import com.marcossial.cartmanager.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ProblemDetail> buildResponse(HttpStatus status, String mensagem, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(status.getReasonPhrase());
        pd.setDetail(mensagem);

        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("caminho", request.getRequestURI());

        return ResponseEntity.status(status).body(pd);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ProblemDetail> tratarRegraDeNegocio(RegraDeNegocioException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> tratarCredenciaisInvalidas(HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> tratarAcessoNegado(HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este recurso.", request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> tratarEntidadeNaoEncontrada(HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado.", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> tratarViolacaoDeIntegridade(HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Conflito de dados. Verifique se a informação já existe.", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> tratarErrosNaoTratados(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor.", request);
    }
}