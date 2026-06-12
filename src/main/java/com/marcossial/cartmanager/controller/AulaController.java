package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.domain.Aula;
import com.marcossial.cartmanager.dto.AulaRequestDTO;
import com.marcossial.cartmanager.service.AulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
public class AulaController {
    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @PostMapping
    public ResponseEntity<Aula> registrarAula(@RequestBody AulaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaService.registrarAula(dto));
    }

    @GetMapping
    public ResponseEntity<List<Aula>> listarAulas() {
        return ResponseEntity.ok(aulaService.listarTodasAulas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aula> encontrarAula(@PathVariable Integer id) {
        return aulaService.encontrarAulaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aula> atualizarAula(@PathVariable Integer id, @RequestBody AulaRequestDTO dto) {
        return aulaService.atualizarAulaPorId(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAula(@PathVariable Integer id) {
        aulaService.excluirAulaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
