package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.domain.Agendamento;
import com.marcossial.cartmanager.dto.AgendamentoRequestDTO;
import com.marcossial.cartmanager.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {
    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<Agendamento> registrarAgendamento(@RequestBody AgendamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.registrarAgendamento(dto));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<Agendamento> cancelarAgendamento(@RequestBody AgendamentoRequestDTO dto) {
        AgendamentoRequestDTO cancelDto = new AgendamentoRequestDTO(dto.data(), dto.aulaId(), dto.carrinhoId(), null, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.registrarAgendamento(cancelDto));
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarPorData(@RequestParam LocalDate data) {
        return ResponseEntity.ok(agendamentoService.listarPorData(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAgendamento(@PathVariable Integer id) {
        agendamentoService.deletarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
