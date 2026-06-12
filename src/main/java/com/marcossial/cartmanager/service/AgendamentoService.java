package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Agendamento;
import com.marcossial.cartmanager.domain.Aula;
import com.marcossial.cartmanager.domain.Carrinho;
import com.marcossial.cartmanager.domain.Professor;
import com.marcossial.cartmanager.domain.Turma;
import com.marcossial.cartmanager.dto.AgendamentoRequestDTO;
import com.marcossial.cartmanager.repository.AgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final CarrinhoService carrinhoService;
    private final AulaService aulaService;
    private final ProfessorService professorService;
    private final TurmaService turmaService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, CarrinhoService carrinhoService, AulaService aulaService, ProfessorService professorService, TurmaService turmaService) {
        this.agendamentoRepository = agendamentoRepository;
        this.carrinhoService = carrinhoService;
        this.aulaService = aulaService;
        this.professorService = professorService;
        this.turmaService = turmaService;
    }

    @Transactional
    public Agendamento registrarAgendamento(AgendamentoRequestDTO dto) {
        Carrinho carrinho = carrinhoService.encontrarCarrinhoPorId(dto.carrinhoId())
                .orElseThrow(() -> new EntityNotFoundException("Carrinho não encontrado"));

        Aula aula = aulaService.encontrarAulaPorId(dto.aulaId())
                .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada"));

        Professor professor = null;
        if (dto.professorId() != null) {
            professor = professorService.encontrarProfessorPorId(dto.professorId())
                    .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));
        }

        Turma turma = null;
        if (dto.turmaId() != null) {
            turma = turmaService.encontrarTurmaPorId(dto.turmaId())
                    .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
        }

        Optional<Agendamento> agendamentoExistente = agendamentoRepository
                .findByDataAndAulaAndCarrinho(dto.data(), aula, carrinho);

        if (agendamentoExistente.isPresent()) {
            Agendamento agendamento = agendamentoExistente.get();
            agendamento.setProfessor(professor);
            agendamento.setTurma(turma);
            return agendamentoRepository.save(agendamento);
        }

        Agendamento novoAgendamento = new Agendamento(
                null,
                dto.data(),
                carrinho,
                professor,
                turma,
                aula
        );

        return agendamentoRepository.save(novoAgendamento);
    }

    public List<Agendamento> listarPorData(LocalDate data) {
        return agendamentoRepository.findByData(data);
    }

    @Transactional
    public void deletarAgendamento(Integer id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Agendamento não encontrado");
        }
        agendamentoRepository.deleteById(id);
    }
}