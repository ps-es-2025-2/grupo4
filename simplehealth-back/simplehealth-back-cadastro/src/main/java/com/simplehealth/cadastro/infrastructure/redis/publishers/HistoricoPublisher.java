package com.simplehealth.cadastro.infrastructure.redis.publishers;

import com.simplehealth.cadastro.domain.events.HistoricoRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricoPublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  public void solicitarAgendamentos(String correlationId, String cpf) {
    redisTemplate.convertAndSend(
        "historico.agendamento.request",
        new HistoricoRequestEvent(correlationId, cpf)
    );
  }

  public void solicitarProcedimentos(String correlationId, String cpf) {
    redisTemplate.convertAndSend(
        "historico.procedimento.request",
        new HistoricoRequestEvent(correlationId, cpf)
    );
  }

  public void solicitarEstoque(String correlationId, String cpf) {
    redisTemplate.convertAndSend(
        "historico.estoque.request",
        new HistoricoRequestEvent(correlationId, cpf)
    );
  }

  public void solicitarPagamentos(String correlationId, String cpf) {
    redisTemplate.convertAndSend(
        "historico.pagamento.request",
        new HistoricoRequestEvent(correlationId, cpf)
    );
  }
}
