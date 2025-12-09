package com.simplehealth.agendamento.infrastructure.redis.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.dtos.ExameResponseDTO;
import com.simplehealth.agendamento.application.dtos.ProcedimentoResponseDTO;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.domain.events.HistoricoAgendamentoResponseEvent;
import com.simplehealth.agendamento.domain.events.HistoricoConsultaResponseEvent;
import com.simplehealth.agendamento.domain.events.HistoricoExameResponseEvent;
import com.simplehealth.agendamento.domain.events.HistoricoProcedimentoResponseEvent;
import com.simplehealth.agendamento.domain.events.HistoricoRequestEvent;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgendamentoSubscriber implements MessageListener {

  private final ConsultaRepository consultaRepository;
  private final ExameRepository exameRepository;
  private final ProcedimentoRepository procedimentoRepository;
  private final ObjectMapper mapper;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String channel = new String(message.getChannel());
      HistoricoRequestEvent requestEvent = mapper.readValue(message.getBody(), HistoricoRequestEvent.class);

      if (requestEvent == null) {
        return;
      }

      log.debug("Recebida solicitação no canal {}. CorrelationID: {}, CPF: {}", channel,
          requestEvent.getCorrelationId(), requestEvent.getCpf());

      switch (channel) {
        case "historico.consulta.request" -> handleConsultaRequest(requestEvent);
        case "historico.exame.request" -> handleExameRequest(requestEvent);
        case "historico.procedimento.request" -> handleProcedimentoRequest(requestEvent);
        case "historico.agendamento.request" -> handleAgendamentoRequest(requestEvent);
        default -> log.warn("Canal desconhecido: {}", channel);
      }

    } catch (Exception e) {
      log.error("Erro ao processar mensagem no AgendamentoSubscriber", e);
    }
  }

  private void handleConsultaRequest(HistoricoRequestEvent requestEvent) {
    List<Consulta> consultas = consultaRepository
        .findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(requestEvent.getCpf());

    List<ConsultaResponseDTO> consultasDto = consultas.stream()
        .map(this::toConsultaDTO)
        .collect(Collectors.toList());

    HistoricoConsultaResponseEvent responseEvent = HistoricoConsultaResponseEvent.builder()
        .correlationId(requestEvent.getCorrelationId())
        .consultas(consultasDto)
        .build();

    redisTemplate.convertAndSend("historico.consulta.response", responseEvent);

    log.debug("Resposta de consultas enviada. CorrelationId: {}, Total: {}",
        responseEvent.getCorrelationId(), consultasDto.size());
  }

  private void handleExameRequest(HistoricoRequestEvent requestEvent) {
    List<Exame> exames = exameRepository
        .findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(requestEvent.getCpf());

    List<ExameResponseDTO> examesDto = exames.stream()
        .map(this::toExameDTO)
        .collect(Collectors.toList());

    HistoricoExameResponseEvent responseEvent = HistoricoExameResponseEvent.builder()
        .correlationId(requestEvent.getCorrelationId())
        .exames(examesDto)
        .build();

    redisTemplate.convertAndSend("historico.exame.response", responseEvent);

    log.debug("Resposta de exames enviada. CorrelationId: {}, Total: {}",
        responseEvent.getCorrelationId(), examesDto.size());
  }

  private void handleProcedimentoRequest(HistoricoRequestEvent requestEvent) {
    List<Procedimento> procedimentos = procedimentoRepository
        .findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(requestEvent.getCpf());

    List<ProcedimentoResponseDTO> procedimentosDto = procedimentos.stream()
        .map(this::toProcedimentoDTO)
        .collect(Collectors.toList());

    HistoricoProcedimentoResponseEvent responseEvent = HistoricoProcedimentoResponseEvent.builder()
        .correlationId(requestEvent.getCorrelationId())
        .procedimentos(procedimentosDto)
        .build();

    redisTemplate.convertAndSend("historico.procedimento.response", responseEvent);

    log.debug("Resposta de procedimentos enviada. CorrelationId: {}, Total: {}",
        responseEvent.getCorrelationId(), procedimentosDto.size());
  }

  private void handleAgendamentoRequest(HistoricoRequestEvent requestEvent) {
    List<Consulta> consultas = consultaRepository
        .findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(requestEvent.getCpf());

    List<AgendamentoDTO> agendamentosDto = consultas.stream()
        .map(consulta -> mapper.convertValue(consulta, AgendamentoDTO.class))
        .collect(Collectors.toList());

    HistoricoAgendamentoResponseEvent responseEvent = HistoricoAgendamentoResponseEvent.builder()
        .correlationId(requestEvent.getCorrelationId())
        .agendamentos(agendamentosDto)
        .build();

    redisTemplate.convertAndSend("historico.agendamento.response", responseEvent);

    log.debug("Resposta de agendamentos enviada. CorrelationId: {}, Total: {}",
        responseEvent.getCorrelationId(), agendamentosDto.size());
  }

  private ConsultaResponseDTO toConsultaDTO(Consulta consulta) {
    return ConsultaResponseDTO.builder()
        .id(consulta.getId())
        .dataHoraAgendamento(consulta.getDataHoraAgendamento())
        .dataHoraInicioPrevista(consulta.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(consulta.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(consulta.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(consulta.getDataHoraFimExecucao())
        .isEncaixe(consulta.getIsEncaixe())
        .modalidade(consulta.getModalidade())
        .motivoEncaixe(consulta.getMotivoEncaixe())
        .observacoes(consulta.getObservacoes())
        .status(consulta.getStatus())
        .motivoCancelamento(consulta.getMotivoCancelamento())
        .dataCancelamento(consulta.getDataCancelamento())
        .pacienteCpf(consulta.getPacienteCpf())
        .medicoCrm(consulta.getMedicoCrm())
        .convenioNome(consulta.getConvenioNome())
        .usuarioCriadorLogin(consulta.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(consulta.getUsuarioCanceladorLogin())
        .especialidade(consulta.getEspecialidade())
        .tipoConsulta(consulta.getTipoConsulta())
        .build();
  }

  private ExameResponseDTO toExameDTO(Exame exame) {
    return ExameResponseDTO.builder()
        .id(exame.getId())
        .dataHoraAgendamento(exame.getDataHoraAgendamento())
        .dataHoraInicioPrevista(exame.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(exame.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(exame.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(exame.getDataHoraFimExecucao())
        .isEncaixe(exame.getIsEncaixe())
        .modalidade(exame.getModalidade())
        .motivoEncaixe(exame.getMotivoEncaixe())
        .observacoes(exame.getObservacoes())
        .status(exame.getStatus())
        .motivoCancelamento(exame.getMotivoCancelamento())
        .dataCancelamento(exame.getDataCancelamento())
        .pacienteCpf(exame.getPacienteCpf())
        .medicoCrm(exame.getMedicoCrm())
        .convenioNome(exame.getConvenioNome())
        .usuarioCriadorLogin(exame.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(exame.getUsuarioCanceladorLogin())
        .nomeExame(exame.getNomeExame())
        .requerPreparo(exame.getRequerPreparo())
        .instrucoesPreparo(exame.getInstrucoesPreparo())
        .build();
  }

  private ProcedimentoResponseDTO toProcedimentoDTO(Procedimento procedimento) {
    return ProcedimentoResponseDTO.builder()
        .id(procedimento.getId())
        .dataHoraAgendamento(procedimento.getDataHoraAgendamento())
        .dataHoraInicioPrevista(procedimento.getDataHoraInicioPrevista())
        .dataHoraFimPrevista(procedimento.getDataHoraFimPrevista())
        .dataHoraInicioExecucao(procedimento.getDataHoraInicioExecucao())
        .dataHoraFimExecucao(procedimento.getDataHoraFimExecucao())
        .isEncaixe(procedimento.getIsEncaixe())
        .modalidade(procedimento.getModalidade())
        .motivoEncaixe(procedimento.getMotivoEncaixe())
        .observacoes(procedimento.getObservacoes())
        .status(procedimento.getStatus())
        .motivoCancelamento(procedimento.getMotivoCancelamento())
        .dataCancelamento(procedimento.getDataCancelamento())
        .pacienteCpf(procedimento.getPacienteCpf())
        .medicoCrm(procedimento.getMedicoCrm())
        .convenioNome(procedimento.getConvenioNome())
        .usuarioCriadorLogin(procedimento.getUsuarioCriadorLogin())
        .usuarioCanceladorLogin(procedimento.getUsuarioCanceladorLogin())
        .descricaoProcedimento(procedimento.getDescricaoProcedimento())
        .salaEquipamentoNecessario(procedimento.getSalaEquipamentoNecessario())
        .nivelRisco(procedimento.getNivelRisco())
        .build();
  }
}