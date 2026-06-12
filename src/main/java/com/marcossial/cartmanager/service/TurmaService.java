package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Turma;
import com.marcossial.cartmanager.dto.TurmaRequestDTO;
import com.marcossial.cartmanager.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurmaService {
    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    // CREATE
    public Turma registrarTurma(TurmaRequestDTO dto) {
        if (dto.nome() == null) {
            throw new RuntimeException("Nome da turma não pode ser nulo");
        }
        return turmaRepository.save(new Turma(null, dto.nome()));
    }

    // READ
    public List<Turma> listarTodasTurmas() {
        return turmaRepository.findAll();
    }

    public Optional<Turma> encontrarTurmaPorId(Integer id) {
        return turmaRepository.findById(id);
    }

    // UPDATE
    public Optional<Turma> atualizarTurmaPorId(Integer id, TurmaRequestDTO dto) {
        return turmaRepository.findById(id).map((turma) -> {
            if (dto.nome() != null) {
                turma.setNome(dto.nome());
            }
            return turmaRepository.save(turma);
        });
    }

    // DELETE
    public void excluirTurmaPorId(Integer id) {
        turmaRepository.deleteById(id);
    }
}
