package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.domain.Professor;
import com.marcossial.cartmanager.dto.ProfessorRequestDTO;
import com.marcossial.cartmanager.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {
    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<Professor> registrarProfessor(@RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.registrarProfessor(dto));
    }

    @GetMapping
    public ResponseEntity<List<Professor>> listarProfessores() {
        return ResponseEntity.ok(professorService.listarTodosProfessores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> encontrarProfessor(@PathVariable Integer id) {
        return professorService.encontrarProfessorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizarProfessor(@PathVariable Integer id, @RequestBody ProfessorRequestDTO dto) {
        return professorService.atualizarProfessorPorId(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProfessor(@PathVariable Integer id) {
        professorService.excluirProfessorPorId(id);
        return ResponseEntity.noContent().build();
    }
}
