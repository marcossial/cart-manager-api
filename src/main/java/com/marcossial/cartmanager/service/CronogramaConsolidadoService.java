package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Agendamento;
import com.marcossial.cartmanager.domain.Aula;
import com.marcossial.cartmanager.domain.Carrinho;
import com.marcossial.cartmanager.domain.CronogramaPadrao;
import com.marcossial.cartmanager.domain.Professor;
import com.marcossial.cartmanager.domain.Reserva;
import com.marcossial.cartmanager.domain.Turma;
import com.marcossial.cartmanager.domain.enums.DiaSemana;
import com.marcossial.cartmanager.dto.CronogramaResponseDTO;
import com.marcossial.cartmanager.dto.enums.OrigemAulaConsolidada;
import com.marcossial.cartmanager.repository.AgendamentoRepository;
import com.marcossial.cartmanager.repository.AulaRepository;
import com.marcossial.cartmanager.repository.CarrinhoRepository;
import com.marcossial.cartmanager.repository.CronogramaPadraoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CronogramaConsolidadoService {

    private final CarrinhoRepository carrinhoRepository;
    private final AulaRepository aulaRepository;
    private final CronogramaPadraoRepository cronogramaPadraoRepository;
    private final AgendamentoRepository agendamentoRepository;

    public CronogramaConsolidadoService(CarrinhoRepository carrinhoRepository,
                                        AulaRepository aulaRepository,
                                        CronogramaPadraoRepository cronogramaPadraoRepository,
                                        AgendamentoRepository agendamentoRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.aulaRepository = aulaRepository;
        this.cronogramaPadraoRepository = cronogramaPadraoRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    public List<CronogramaResponseDTO> obterCronogramaConsolidadoDiario(LocalDate data) {
        List<CronogramaResponseDTO> cronogramasIndividuais = carrinhoRepository.findAll().stream()
                .flatMap(carrinho -> obterCronogramaConsolidado(data, carrinho.getId()).stream())
                .toList();

        Map<Integer, List<CronogramaResponseDTO>> agrupadoPorAula = cronogramasIndividuais.stream()
                .collect(Collectors.groupingBy(CronogramaResponseDTO::numeroAula));

        return agrupadoPorAula.entrySet().stream()
                .map(entry -> {
                    List<CronogramaResponseDTO> dtosDaAula = entry.getValue();
                    CronogramaResponseDTO base = dtosDaAula.get(0);

                    Reserva[] todasReservas = dtosDaAula.stream()
                            .flatMap(dto -> Arrays.stream(dto.reservas()))
                            .toArray(Reserva[]::new);

                    OrigemAulaConsolidada origemGeral = dtosDaAula.stream()
                            .map(CronogramaResponseDTO::origem)
                            .filter(o -> o == OrigemAulaConsolidada.ALTERACAO)
                            .findFirst()
                            .orElse(OrigemAulaConsolidada.PADRAO);

                    return new CronogramaResponseDTO(
                            base.numeroAula(),
                            base.horaInicio(),
                            base.horaFim(),
                            todasReservas,
                            origemGeral
                    );
                })
                .sorted(Comparator.comparing(CronogramaResponseDTO::numeroAula))
                .toList();
    }

    public List<CronogramaResponseDTO> obterCronogramaConsolidado(LocalDate data, Integer carrinhoId) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new EntityNotFoundException("Carrinho não encontrado"));

        DiaSemana diaSemana = mapearDiaSemana(data.getDayOfWeek());

        List<Aula> todasAulas = aulaRepository.findAll();
        todasAulas.sort(Comparator.comparing(Aula::getNumeroAula));

        List<CronogramaPadrao> cronogramaPadraoList = (diaSemana != null) ?
                cronogramaPadraoRepository.findByCarrinhoAndDiaSemana(carrinho, diaSemana) :
                Collections.emptyList();

        List<Agendamento> agendamentosList = agendamentoRepository.findByDataAndCarrinho(data, carrinho);

        Map<Integer, CronogramaPadrao> padraoMap = cronogramaPadraoList.stream()
                .collect(Collectors.toMap(c -> c.getAula().getId(), c -> c));

        Map<Integer, Agendamento> agendamentoMap = agendamentosList.stream()
                .collect(Collectors.toMap(a -> a.getAula().getId(), a -> a));

        return todasAulas.stream().map(aula -> {
            Agendamento agendamento = agendamentoMap.get(aula.getId());
            if (agendamento != null) {
                if (agendamento.getProfessor() == null) {
                    return criarDto(aula, carrinho, null, null, OrigemAulaConsolidada.LIVRE);
                } else {
                    return criarDto(aula, carrinho, agendamento.getProfessor(), agendamento.getTurma(), OrigemAulaConsolidada.ALTERACAO);
                }
            }

            CronogramaPadrao padrao = padraoMap.get(aula.getId());
            if (padrao != null) {
                return criarDto(aula, carrinho, padrao.getProfessor(), padrao.getTurma(), OrigemAulaConsolidada.PADRAO);
            }

            return criarDto(aula, carrinho, null, null, OrigemAulaConsolidada.LIVRE);
        }).collect(Collectors.toList());
    }

    private DiaSemana mapearDiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.SEGUNDA;
            case TUESDAY -> DiaSemana.TERCA;
            case WEDNESDAY -> DiaSemana.QUARTA;
            case THURSDAY -> DiaSemana.QUINTA;
            case FRIDAY -> DiaSemana.SEXTA;
            default -> null;
        };
    }

    private CronogramaResponseDTO criarDto(Aula aula, Carrinho carrinho, Professor professor, Turma turma, OrigemAulaConsolidada origem) {
        String professorNome = (professor != null) ? professor.getNome() : null;
        String turmaNome = (turma != null) ? turma.getNome() : null;

        Reserva reserva = new Reserva(carrinho.getNome(), turmaNome, professorNome);

        return new CronogramaResponseDTO(
                (int) aula.getNumeroAula(),
                aula.getHoraInicio(),
                aula.getHoraFim(),
                new Reserva[]{reserva},
                origem
        );
    }
}
