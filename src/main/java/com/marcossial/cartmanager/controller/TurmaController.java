package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.domain.Turma;
import com.marcossial.cartmanager.dto.TurmaRequestDTO;
import com.marcossial.cartmanager.service.TurmaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {
    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping
    public ResponseEntity<Turma> registrarTurma(@RequestBody TurmaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaService.registrarTurma(dto));
    }

    @GetMapping
    public ResponseEntity<List<Turma>> listarTurmas() {
        return ResponseEntity.ok(turmaService.listarTodasTurmas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turma> encontrarTurma(@PathVariable Integer id) {
        return turmaService.encontrarTurmaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizarTurma(@PathVariable Integer id, @RequestBody TurmaRequestDTO dto) {
        return turmaService.atualizarTurmaPorId(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTurma(@PathVariable Integer id) {
        turmaService.excluirTurmaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
