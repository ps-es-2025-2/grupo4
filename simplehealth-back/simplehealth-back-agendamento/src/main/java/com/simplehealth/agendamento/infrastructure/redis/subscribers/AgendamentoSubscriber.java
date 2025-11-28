package com.simplehealth.agendamento.infrastructure.redis.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.events.HistoricoAgendamentoResponseEvent;
import com.simplehealth.agendamento.domain.events.HistoricoRequestEvent;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
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
  private final ObjectMapper mapper;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      HistoricoRequestEvent requestEvent = mapper.readValue(message.getBody(), HistoricoRequestEvent.class);

      if (requestEvent == null) {
        return;
      }

      log.debug("Recebida solicitação. CorrelationID: {}, CPF: {}", requestEvent.getCorrelationId(),
          requestEvent.getCpf());

      List<Consulta> consultas = consultaRepository
          .findByPacienteCpfOrderByDataHoraInicioDesc(requestEvent.getCpf());

      List<AgendamentoDTO> agendamentosDto = consultas.stream()
          .map(consulta -> mapper.convertValue(consulta, AgendamentoDTO.class))
          .collect(Collectors.toList());

      HistoricoAgendamentoResponseEvent responseEvent = HistoricoAgendamentoResponseEvent.builder()
          .correlationId(requestEvent.getCorrelationId())
          .agendamentos(agendamentosDto)
          .build();

      redisTemplate.convertAndSend(
          "historico.agendamento.response",
          responseEvent
      );

      log.debug("Resposta enviada para correlationId: {}. Total de itens: {}",
          responseEvent.getCorrelationId(), agendamentosDto.size());

    } catch (Exception e) {
      log.error("Erro ao processar mensagem no AgendamentoSubscriber", e);
    }
  }
}