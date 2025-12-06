package com.simplehealth.cadastro.infrastructure.redis.publishers;

import com.simplehealth.cadastro.domain.events.EstoqueAlertaRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EstoqueAlertaPublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  public void solicitarAlertasEstoqueCritico(String correlationId) {
    redisTemplate.convertAndSend(
        "estoque.alerta.request",
        new EstoqueAlertaRequestEvent(correlationId));
  }
}
