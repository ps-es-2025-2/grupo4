package com.simplehealth.cadastro.infrastructure.redis.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.cadastro.domain.events.*;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricoResponseSubscriber implements MessageListener {

  private final ObjectMapper mapper;
  private final ConcurrentHashMap<String, Object> cache;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String channel = new String(message.getChannel());
      String payload = new String(message.getBody());
      System.out.println("Mensagem recebida: " + payload);

      switch (channel) {
        case "historico.agendamento.response" -> {
          HistoricoAgendamentoResponseEvent event = mapper.readValue(payload, HistoricoAgendamentoResponseEvent.class);
          cache.put(event.getCorrelationId() + ":ag", event);
        }
        case "historico.procedimento.response" -> {
          HistoricoProcedimentoResponseEvent event = mapper.readValue(payload, HistoricoProcedimentoResponseEvent.class);
          cache.put(event.getCorrelationId() + ":proc", event);
        }
        case "historico.estoque.response" -> {
          HistoricoEstoqueResponseEvent event = mapper.readValue(payload, HistoricoEstoqueResponseEvent.class);
          cache.put(event.getCorrelationId() + ":est", event);
        }
        case "historico.pagamento.response" -> {
          HistoricoPagamentoResponseEvent event = mapper.readValue(payload, HistoricoPagamentoResponseEvent.class);
          cache.put(event.getCorrelationId() + ":pag", event);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Erro ao processar mensagem do Redis", e);
    }
  }

}
