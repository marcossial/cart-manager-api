package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Professor;
import com.marcossial.cartmanager.dto.ProfessorRequestDTO;
import com.marcossial.cartmanager.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    // CREATE
    public Professor registrarProfessor(ProfessorRequestDTO dto) {
        if (dto.nome() == null) {
            throw new RuntimeException("Nome do professor não pode ser nulo");
        }
        return professorRepository.save(new Professor(null,dto.nome()));
    }

    // READ
    public List<Professor> listarTodosProfessores() {
        return professorRepository.findAll();
    }

    public Optional<Professor> encontrarProfessorPorId(Integer id) {
        return professorRepository.findById(id);
    }

    // UPDATE
    public Optional<Professor> atualizarProfessorPorId(Integer id, ProfessorRequestDTO dto) {
        return professorRepository.findById(id).map((prof) -> {
            if (dto.nome() != null) {
                prof.setNome(dto.nome());
            }
            return professorRepository.save(prof);
        });
    }

    // DELETE
    public void excluirProfessorPorId(Integer id) {
        professorRepository.deleteById(id);
    }
}
