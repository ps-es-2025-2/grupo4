package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.AlertaEstoqueDTO;
import com.simplehealth.cadastro.domain.events.EstoqueAlertaResponseEvent;
import com.simplehealth.cadastro.infrastructure.redis.publishers.EstoqueAlertaPublisher;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GerarAlertaEstoqueCriticoUseCase {

  private final EstoqueAlertaPublisher publisher;
  private final ConcurrentHashMap<String, Object> cache;

  public List<AlertaEstoqueDTO> execute() {
    String correlationId = UUID.randomUUID().toString();

    publisher.solicitarAlertasEstoqueCritico(correlationId);

    List<AlertaEstoqueDTO> alertas = Collections.emptyList();

    int tentativas = 0;
    final int maxTentativas = 50;

    while (tentativas < maxTentativas) {
      var response = cache.get(correlationId + ":alerta");

      if (response instanceof EstoqueAlertaResponseEvent) {
        alertas = ((EstoqueAlertaResponseEvent) response).getAlertas();
        if (alertas == null) {
          alertas = Collections.emptyList();
        }
        cache.remove(correlationId + ":alerta");
        break;
      }

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }

      tentativas++;
    }

    return alertas;
  }
}
