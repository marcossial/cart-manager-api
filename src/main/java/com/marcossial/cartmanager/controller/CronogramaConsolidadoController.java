package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.dto.CronogramaResponseDTO;
import com.marcossial.cartmanager.service.CronogramaConsolidadoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cronograma")
public class CronogramaConsolidadoController {
    private final CronogramaConsolidadoService cronogramaConsolidadoService;

    public CronogramaConsolidadoController(CronogramaConsolidadoService cronogramaConsolidadoService) {
        this.cronogramaConsolidadoService = cronogramaConsolidadoService;
    }

    @GetMapping
    public ResponseEntity<List<CronogramaResponseDTO>> obterCronograma(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Integer carrinhoId) {
        
        if (carrinhoId != null) {
            return ResponseEntity.ok(cronogramaConsolidadoService.obterCronogramaConsolidado(data, carrinhoId));
        } else {
            return ResponseEntity.ok(cronogramaConsolidadoService.obterCronogramaConsolidadoDiario(data));
        }
    }
}
