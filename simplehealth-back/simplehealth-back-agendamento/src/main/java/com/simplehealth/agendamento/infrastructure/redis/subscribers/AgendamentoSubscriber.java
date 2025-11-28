package com.simplehealth.agendamento.infrastructure.redis.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.events.HistoricoRequestEvent;
import com.simplehealth.agendamento.infrastructure.repositories.AgendamentoRepository;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgendamentoSubscriber implements MessageListener {

  private final ConsultaRepository consultaRepository;
  private final ObjectMapper mapper;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      HistoricoRequestEvent event = mapper.readValue(message.getBody(), HistoricoRequestEvent.class);
      if (event == null) return;

      List<Consulta> historico = consultaRepository
          .findByPacienteCpfOrderByDataHoraInicioDesc(event.getCpf());

      String payload = mapper.writeValueAsString(historico);

      redisTemplate.convertAndSend(
          "historico.agendamento.response",
          payload
      );

    } catch (Exception ignored) {
    }
  }
}
