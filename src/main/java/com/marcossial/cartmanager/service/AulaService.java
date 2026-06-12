package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Aula;
import com.marcossial.cartmanager.dto.AulaRequestDTO;
import com.marcossial.cartmanager.repository.AulaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaService {
    private final AulaRepository aulaRepository;

    public AulaService(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    // CREATE
    public Aula registrarAula(AulaRequestDTO dto) {
        if (dto.numeroAula() == null || dto.horaInicio() == null || dto.horaFim() == null) {
            throw new RuntimeException("Numero da Aula, horario de inicio e fim não podem ser nulos");
        }
        return aulaRepository.save(new Aula(null, dto.numeroAula(), dto.horaInicio(), dto.horaFim()));
    }

    // READ
    public List<Aula> listarTodasAulas() {
        return aulaRepository.findAll();
    }

    public Optional<Aula> encontrarAulaPorId(Integer id) {
        return aulaRepository.findById(id);
    }

    public Optional<Aula> encontrarAulaPorNumero(Short numeroAula) {
        return aulaRepository.findByNumeroAula(numeroAula);
    }

    // UPDATE
    public Optional<Aula> atualizarAulaPorId(Integer id, AulaRequestDTO dto) {
        return aulaRepository.findById(id).map((aula) -> {
            if (dto.numeroAula() != null) {
                aula.setNumeroAula(dto.numeroAula());
            }
            if (dto.horaInicio() != null) {
                aula.setHoraInicio(dto.horaInicio());
            }
            if (dto.horaFim() != null) {
                aula.setHoraFim(dto.horaFim());
            }
            return aulaRepository.save(aula);
        });
    }

    // DELETE
    public void excluirAulaPorId(Integer id) {
        aulaRepository.deleteById(id);
    }

}
