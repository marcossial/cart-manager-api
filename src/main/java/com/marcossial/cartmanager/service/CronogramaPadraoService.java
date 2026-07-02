package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Aula;
import com.marcossial.cartmanager.domain.Carrinho;
import com.marcossial.cartmanager.domain.CronogramaPadrao;
import com.marcossial.cartmanager.domain.Professor;
import com.marcossial.cartmanager.domain.Turma;
import com.marcossial.cartmanager.domain.enums.DiaSemana;
import com.marcossial.cartmanager.dto.CronogramaPadraoRequestDTO;
import com.marcossial.cartmanager.repository.CronogramaPadraoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CronogramaPadraoService {
    private final CronogramaPadraoRepository cronogramaPadraoRepository;
    private final CarrinhoService carrinhoService;
    private final ProfessorService professorService;
    private final TurmaService turmaService;
    private final AulaService aulaService;

    public CronogramaPadraoService(CronogramaPadraoRepository cronogramaPadraoRepository, CarrinhoService carrinhoService, ProfessorService professorService, TurmaService turmaService, AulaService aulaService) {
        this.cronogramaPadraoRepository = cronogramaPadraoRepository;
        this.carrinhoService = carrinhoService;
        this.professorService = professorService;
        this.turmaService = turmaService;
        this.aulaService = aulaService;
    }

    // CREATE
    @Transactional
    public CronogramaPadrao registrarCronogramaPadrao(CronogramaPadraoRequestDTO dto) {
        Carrinho carrinho = carrinhoService.encontrarCarrinhoPorId(dto.carrinhoId())
                .orElseThrow(() -> new EntityNotFoundException("Carrinho de ID : " + dto.carrinhoId() + " não encontrado"));
        Professor professor = null;
        if (dto.professorId() != null) {
            professor = professorService.encontrarProfessorPorId(dto.professorId())
                    .orElseThrow(() -> new EntityNotFoundException("Professor de ID : " + dto.professorId() + " não encontrado"));
        }
        Turma turma = turmaService.encontrarTurmaPorId(dto.turmaId())
                .orElseThrow(() -> new EntityNotFoundException("Turma de ID : " + dto.turmaId() + " não encontrada"));
        Aula aula = aulaService.encontrarAulaPorId(dto.aulaId())
                .orElseThrow(() -> new EntityNotFoundException("Aula de ID : " + dto.aulaId() + " não encontrada"));;

        if (cronogramaPadraoRepository.existsByDiaSemanaAndAulaAndCarrinho(dto.diaSemana(), aula, carrinho)) {
            throw new EntityExistsException("Carrinho " + carrinho.getNome() + " já agendado para a aula " + aula.getNumeroAula() + " na " + dto.diaSemana());
        }

        return cronogramaPadraoRepository.save(new CronogramaPadrao(
                null,
                dto.diaSemana(),
                carrinho,
                professor,
                turma,
                aula
        ));

    }

    // READ
    public List<CronogramaPadrao> listarCronogramaPadrao() {
        return cronogramaPadraoRepository.findAll();
    }

    public List<CronogramaPadrao> listarCronogramaPadraoPorNomeCarrinho(String carrinhoNome) {
        Carrinho carrinho = carrinhoService.encontrarCarrinhoPorNome(carrinhoNome)
                .orElseThrow(() -> new EntityNotFoundException("Carrinho não encontrado"));
        return cronogramaPadraoRepository.findByCarrinho(carrinho);
    }

    public List<CronogramaPadrao> listarCronogramaPadraoPorIdCarrinho(Integer carrinhoId) {
        Carrinho carrinho = carrinhoService.encontrarCarrinhoPorId(carrinhoId)
                .orElseThrow(() -> new EntityNotFoundException("Carrinho não encontrado"));
        return cronogramaPadraoRepository.findByCarrinho(carrinho);
    }

    public List<CronogramaPadrao> listarCronogramaPadraoPorDia(DiaSemana diaSemana) {
        return cronogramaPadraoRepository.findByDiaSemana(diaSemana);
    }

    public List<CronogramaPadrao> listarCronogramaPadraoPorIdAula(Integer aulaId) {
        Aula aula = aulaService.encontrarAulaPorId(aulaId)
                .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada"));
        return cronogramaPadraoRepository.findByAula(aula);
    }

    public List<CronogramaPadrao> listarCronogramaPadraoPorNumeroAula(Short numeroAula) {
        Aula aula = aulaService.encontrarAulaPorNumero(numeroAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada"));
        return cronogramaPadraoRepository.findByAula(aula);
    }

    // UPDATE
    public CronogramaPadrao atualizarCronogramaPadraoPorId(Integer id, CronogramaPadraoRequestDTO dto) {
        return cronogramaPadraoRepository.findById(id).map(existente -> {
            Carrinho carrinho = carrinhoService.encontrarCarrinhoPorId(dto.carrinhoId())
                    .orElseThrow(() -> new EntityNotFoundException("Carrinho de ID : " + dto.carrinhoId() + " não encontrado"));
            Professor professor = null;
            if (dto.professorId() != null) {
                professor = professorService.encontrarProfessorPorId(dto.professorId())
                        .orElseThrow(() -> new EntityNotFoundException("Professor de ID : " + dto.professorId() + " não encontrado"));
            }
            Turma turma = turmaService.encontrarTurmaPorId(dto.turmaId())
                    .orElseThrow(() -> new EntityNotFoundException("Turma de ID : " + dto.turmaId() + " não encontrada"));
            Aula aula = aulaService.encontrarAulaPorId(dto.aulaId())
                    .orElseThrow(() -> new EntityNotFoundException("Aula de ID : " + dto.aulaId() + " não encontrada"));

            boolean conflito = cronogramaPadraoRepository.existsByDiaSemanaAndAulaAndCarrinho(dto.diaSemana(), aula, carrinho);

            if (conflito && (!existente.getDiaSemana().equals(dto.diaSemana()) || !existente.getAula().equals(aula) || !existente.getCarrinho().equals(carrinho))) {
                throw new EntityExistsException("Conflito de horário detectado na atualização.");
            }

            existente.setDiaSemana(dto.diaSemana());
            existente.setCarrinho(carrinho);
            existente.setAula(aula);
            existente.setProfessor(professor);
            existente.setTurma(turma);
            return cronogramaPadraoRepository.save(existente);
        }).orElseThrow(() -> new EntityNotFoundException("Cronograma de ID: " + id + " não encontrado"));
    }

    // DELETE
    public void excluirCronogramaPadraoPorId(Integer id) {
        cronogramaPadraoRepository.deleteById(id);
    }
}
