package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.*;
import com.marcossial.cartmanager.domain.enums.DiaSemana;
import com.marcossial.cartmanager.domain.enums.TipoSemana;
import com.marcossial.cartmanager.dto.CronogramaResponseDTO;
import com.marcossial.cartmanager.dto.enums.OrigemAulaConsolidada;
import com.marcossial.cartmanager.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
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

    /**
     * Obtém o cronograma consolidado diário de TODOS os carrinhos.
     */
    public List<CronogramaResponseDTO> obterCronogramaConsolidadoDiario(LocalDate data, boolean somentePadrao) {
        TipoSemana tipoSemanaAtual = determinarTipoSemanaDaData(data);
        DiaSemana diaSemana = mapearDiaSemana(data.getDayOfWeek());

        List<Aula> todasAulas = aulaRepository.findAll();
        todasAulas.sort(Comparator.comparing(Aula::getNumeroAula));

        List<Carrinho> todosCarrinhos = carrinhoRepository.findAll();

        if (diaSemana == null || todosCarrinhos.isEmpty()) {
            return Collections.emptyList();
        }

        List<TipoSemana> tiposValidos = List.of(TipoSemana.AMBAS, tipoSemanaAtual);

        Map<Integer, Map<Integer, CronogramaPadrao>> padraoPorCarrinhoEAula = cronogramaPadraoRepository
                .findByDiaSemanaAndTipoSemanaIn(diaSemana, tiposValidos)
                .stream()
                .collect(Collectors.groupingBy(
                        cp -> cp.getCarrinho().getId(),
                        Collectors.toMap(
                                cp -> cp.getAula().getId(),
                                cp -> cp,
                                (ex, nv) -> ex.getTipoSemana() != TipoSemana.AMBAS ? ex : nv
                        )
                ));

        Map<Integer, Map<Integer, Agendamento>> agendamentosPorCarrinhoEAula = somentePadrao ? Collections.emptyMap() :
                agendamentoRepository.findByData(data).stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getCarrinho().getId(),
                                Collectors.toMap(a -> a.getAula().getId(), a -> a)
                        ));

        return todasAulas.stream().map(aula -> {
            List<Reserva> reservas = new ArrayList<>();
            boolean teveAlteracao = false;

            for (Carrinho carrinho : todosCarrinhos) {
                Agendamento agendamento = agendamentosPorCarrinhoEAula
                        .getOrDefault(carrinho.getId(), Collections.emptyMap())
                        .get(aula.getId());

                if (!somentePadrao && agendamento != null) {
                    if (agendamento.getProfessor() != null) {
                        reservas.add(new Reserva(carrinho.getNome(), agendamento.getTurma().getNome(), agendamento.getProfessor().getNome()));
                        teveAlteracao = true;
                    }
                    continue;
                }

                CronogramaPadrao padrao = padraoPorCarrinhoEAula
                        .getOrDefault(carrinho.getId(), Collections.emptyMap())
                        .get(aula.getId());

                if (padrao != null && padrao.getProfessor() != null) {
                    reservas.add(new Reserva(carrinho.getNome(), padrao.getTurma().getNome(), padrao.getProfessor().getNome()));
                }
            }

            OrigemAulaConsolidada origem = teveAlteracao ? OrigemAulaConsolidada.ALTERACAO : OrigemAulaConsolidada.PADRAO;

            return new CronogramaResponseDTO(
                    (int) aula.getNumeroAula(),
                    aula.getHoraInicio(),
                    aula.getHoraFim(),
                    tipoSemanaAtual,
                    reservas.toArray(new Reserva[0]),
                    origem
            );
        }).toList();
    }

    /**
     * Obtém o cronograma de UM ÚNICO carrinho específico.
     */
    public List<CronogramaResponseDTO> obterCronogramaConsolidado(LocalDate data, Integer carrinhoId, boolean somentePadrao) {
        TipoSemana tipoSemanaAtual = determinarTipoSemanaDaData(data);
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new EntityNotFoundException("Carrinho não encontrado"));

        DiaSemana diaSemana = mapearDiaSemana(data.getDayOfWeek());
        List<Aula> todasAulas = aulaRepository.findAll();
        todasAulas.sort(Comparator.comparing(Aula::getNumeroAula));

        List<TipoSemana> tiposValidos = List.of(TipoSemana.AMBAS, tipoSemanaAtual);

        Map<Integer, CronogramaPadrao> padraoMap = (diaSemana != null) ?
                cronogramaPadraoRepository.findByCarrinhoAndDiaSemanaAndTipoSemanaIn(carrinho, diaSemana, tiposValidos).stream()
                        .collect(Collectors.toMap(
                                c -> c.getAula().getId(),
                                c -> c,
                                (ex, nv) -> ex.getTipoSemana() != TipoSemana.AMBAS ? ex : nv
                        )) : Collections.emptyMap();

        Map<Integer, Agendamento> agendamentoMap = somentePadrao ? Collections.emptyMap() :
                agendamentoRepository.findByDataAndCarrinho(data, carrinho).stream()
                        .collect(Collectors.toMap(a -> a.getAula().getId(), a -> a));

        return todasAulas.stream().map(aula -> {
            if (!somentePadrao) {
                Agendamento agendamento = agendamentoMap.get(aula.getId());
                if (agendamento != null) {
                    Professor p = agendamento.getProfessor();
                    Turma t = agendamento.getTurma();
                    OrigemAulaConsolidada origem = (p == null) ? OrigemAulaConsolidada.LIVRE : OrigemAulaConsolidada.ALTERACAO;
                    return criarDto(aula, carrinho, p, t, origem, tipoSemanaAtual);
                }
            }

            CronogramaPadrao padrao = padraoMap.get(aula.getId());
            if (padrao != null) {
                return criarDto(aula, carrinho, padrao.getProfessor(), padrao.getTurma(), OrigemAulaConsolidada.PADRAO, tipoSemanaAtual);
            }

            return criarDto(aula, carrinho, null, null, OrigemAulaConsolidada.LIVRE, tipoSemanaAtual);
        }).toList();
    }

    private TipoSemana determinarTipoSemanaDaData(LocalDate data) {
        int numeroDaSemanaNoAno = data.get(WeekFields.ISO.weekOfWeekBasedYear());
        return (numeroDaSemanaNoAno % 2 == 0) ? TipoSemana.PAR : TipoSemana.IMPAR;
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

    private CronogramaResponseDTO criarDto(Aula aula, Carrinho carrinho, Professor professor, Turma turma, OrigemAulaConsolidada origem, TipoSemana tipoSemana) {
        String professorNome = (professor != null) ? professor.getNome() : null;
        String turmaNome = (turma != null) ? turma.getNome() : null;

        Reserva reserva = new Reserva(carrinho.getNome(), turmaNome, professorNome);

        return new CronogramaResponseDTO(
                (int) aula.getNumeroAula(),
                aula.getHoraInicio(),
                aula.getHoraFim(),
                tipoSemana,
                new Reserva[]{reserva},
                origem
        );
    }
}