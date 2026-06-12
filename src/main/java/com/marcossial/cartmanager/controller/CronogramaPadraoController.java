package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.domain.CronogramaPadrao;
import com.marcossial.cartmanager.dto.CronogramaPadraoRequestDTO;
import com.marcossial.cartmanager.service.CronogramaPadraoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cronograma-padrao")
public class CronogramaPadraoController {
    private final CronogramaPadraoService cronogramaPadraoService;

    public CronogramaPadraoController(CronogramaPadraoService cronogramaPadraoService) {
        this.cronogramaPadraoService = cronogramaPadraoService;
    }

    @PostMapping
    public ResponseEntity<CronogramaPadrao> registrarCronogramaPadrao(@RequestBody CronogramaPadraoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cronogramaPadraoService.registrarCronogramaPadrao(dto));
    }

    @GetMapping
    public ResponseEntity<List<CronogramaPadrao>> listarCronogramas() {
        return ResponseEntity.ok(cronogramaPadraoService.listarCronogramaPadrao());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CronogramaPadrao> atualizarCronogramaPadrao(@PathVariable Integer id, @RequestBody CronogramaPadraoRequestDTO dto) {
        return ResponseEntity.ok(cronogramaPadraoService.atualizarCronogramaPadraoPorId(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCronogramaPadrao(@PathVariable Integer id) {
        cronogramaPadraoService.excluirCronogramaPadraoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
