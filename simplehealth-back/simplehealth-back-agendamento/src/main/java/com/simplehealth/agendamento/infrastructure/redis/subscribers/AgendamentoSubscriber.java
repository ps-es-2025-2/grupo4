package com.simplehealth.agendamento.infrastructure.redis.subscribers;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.events.HistoricoRequestEvent;
import com.simplehealth.agendamento.infrastructure.repositories.AgendamentoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgendamentoSubscriber implements MessageListener {

  private final AgendamentoRepository agendamentoRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    HistoricoRequestEvent event = (HistoricoRequestEvent) redisTemplate.getValueSerializer()
        .deserialize(message.getBody());
    if (event == null) {
      return;
    }

    List<Agendamento> historico = agendamentoRepository
        .findByPacienteCpfOrderByDataHoraInicioDesc(event.getCpf());

    redisTemplate.convertAndSend(
        "historico.agendamento.response." + event.getCorrelationId(),
        historico
    );
  }
}
